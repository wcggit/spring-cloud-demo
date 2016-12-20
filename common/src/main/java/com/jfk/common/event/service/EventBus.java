package com.jfk.common.event.service;

import com.google.common.base.Stopwatch;

import com.jfk.base.api.BooleanWrapper;
import com.jfk.base.event.constants.EventType;
import com.jfk.base.event.constants.FailureInfo;
import com.jfk.base.event.constants.FailureReason;
import com.jfk.base.event.domain.AskEvent;
import com.jfk.base.event.domain.AskResponseEvent;
import com.jfk.base.event.domain.BaseEvent;
import com.jfk.base.event.domain.NotifyEvent;
import com.jfk.base.event.domain.Revokable;
import com.jfk.base.event.domain.RevokeAskEvent;
import com.jfk.base.exception.AppBusinessException;
import com.jfk.common.event.AskParameter;
import com.jfk.common.event.EventRegistry;
import com.jfk.common.event.EventUtils;
import com.jfk.common.event.constant.AskEventStatus;
import com.jfk.common.event.constant.EventCategory;
import com.jfk.common.event.constant.ProcessStatus;
import com.jfk.common.event.domain.*;
import com.jfk.common.event.handler.AskEventHandler;
import com.jfk.common.event.handler.NotifyEventHandler;
import com.jfk.common.event.handler.RevokableAskEventHandler;
import com.jfk.common.event.mapper.AskRequestEventPublishMapper;
import com.jfk.common.event.mapper.AskResponseEventPublishMapper;
import com.jfk.common.event.mapper.EventProcessMapper;
import com.jfk.common.event.mapper.NotifyEventPublishMapper;
import com.jfk.common.event.mapper.RevokeAskEventPublishMapper;
import com.jfk.common.exception.EventException;
import com.jfk.common.spring.ApplicationConstant;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by liubin on 2016/4/8.
 */
@Service
public class EventBus {

  private static Logger logger = LoggerFactory.getLogger(EventBus.class);


  @Autowired
  protected EventProcessMapper eventProcessMapper;

  @Autowired
  protected EventActivator eventActivator;

  @Autowired
  protected TaskExecutor taskExecutor;

  @Autowired
  protected EventRegistry eventRegistry;

  @Autowired
  protected EventWatchService eventWatchService;

  @Autowired
  protected EventPublishService eventPublishService;

  @Autowired
  protected EventHandlerExecutor eventHandlerExecutor;

  @Autowired
  private NotifyEventPublishMapper notifyEventPublishMapper;

  @Autowired
  private AskRequestEventPublishMapper askRequestEventPublishMapper;

  @Autowired
  private RevokeAskEventPublishMapper revokeAskEventPublishMapper;

  @Autowired
  private AskResponseEventPublishMapper askResponseEventPublishMapper;

  @Autowired
  private ApplicationConstant applicationConstant;

  /**
   * 发布Notify事件
   */
  @Transactional
  public NotifyEventPublish publish(NotifyEvent notifyEvent) {

    fillEventId(notifyEvent);
    String payload = EventUtils.serializeEvent(notifyEvent);

    NotifyEventPublish eventPublish = new NotifyEventPublish();
    eventPublish.setPayload(payload);
    eventPublish.setEventId(notifyEvent.getId());
    eventPublish.setEventType(notifyEvent.getType());

    notifyEventPublishMapper.insert(eventPublish);
    return eventPublish;
  }

  /**
   * 发布ask事件
   */
  @Transactional
  public List<AskRequestEventPublish> ask(AskParameter askParameter) {

    askParameter.getAskEvents().forEach(this::fillEventId);

    EventWatch eventWatch = eventWatchService.watchAskEvents(askParameter);

    return askParameter.getAskEvents().stream().map(askEvent -> {
      AskRequestEventPublish eventPublish = new AskRequestEventPublish();
      eventPublish.setEventId(askEvent.getId());
      eventPublish.setEventType(askEvent.getType());
      eventPublish.setAskEventStatus(AskEventStatus.PENDING.name());
      eventPublish.setWatchId(eventWatch.getId());
      eventPublish.setPayload(EventUtils.serializeEvent(askEvent));

      askRequestEventPublishMapper.insert(eventPublish);

      return eventPublish;

    }).collect(Collectors.toList());
  }

  /**
   * 尝试对事件进行撤销
   */
  @Transactional
  public void revoke(AskEvent askEvent, FailureInfo failureInfo) {
    if (!(askEvent instanceof Revokable)) {
      throw new EventException(String.format("类型为%s的事件不能撤销", askEvent.getClass()));
    }
    if (askEvent.getId() == null) {
      throw new EventException("ID为空, 新事件不能撤销");
    }
    AskRequestEventPublish
        eventPublish =
        eventPublishService.getAskRequestEventByEventId(askEvent.getId());
    if (eventPublish.getStatus().equals(ProcessStatus.NEW.name())) {
      //首先判断原事件有没有发送, 如果没有发送就不发送了
      eventPublish.setStatus(ProcessStatus.IGNORE.name());
      askRequestEventPublishMapper.insert(eventPublish);
    }

    if (eventPublish.getAskEventStatus().equals(AskEventStatus.PENDING.name())
        || eventPublish.getAskEventStatus().equals(AskEventStatus.SUCCESS.name())) {

      if (eventPublish.getStatus().equals(ProcessStatus.PROCESSED.name())) {
        publishRevokeEvent(askEvent.getId(), failureInfo);
      }

      //改变之前事件的状态
      AskEventStatus revokeAskEventStatus = AskEventStatus.FAILED;
      if (failureInfo != null && failureInfo.getReason() != null) {
        revokeAskEventStatus = EventUtils.fromFailureReason(failureInfo.getReason());
      }
      eventPublish.setAskEventStatus(revokeAskEventStatus.name());

      askRequestEventPublishMapper.insert(eventPublish);

      //TODO AskRequestEventPublish 状态已经改变, 根据watchId判断eventWatch是不是也要改变

    }
  }

  /**
   * 发布撤销事件
   */
  @Transactional
  public void publishRevokeEvent(Long askEventId, FailureInfo failureInfo) {
    RevokeAskEvent revokeAskEvent = new RevokeAskEvent(failureInfo, askEventId);
    fillEventId(revokeAskEvent);

    RevokeAskEventPublish revokeAskEventPublish = new RevokeAskEventPublish();
    revokeAskEventPublish.setAskEventId(askEventId);
    revokeAskEventPublish.setEventId(revokeAskEvent.getId());
    revokeAskEventPublish.setEventType(revokeAskEvent.getType());
    revokeAskEventPublish.setPayload(EventUtils.serializeEvent(revokeAskEvent));

    revokeAskEventPublishMapper.insert(revokeAskEventPublish);
  }


  @SuppressWarnings("unchecked")
  @Transactional
  public void sendUnpublishedEvent() {

    List<EventPublish> events = eventPublishService.findUnpublishedEvent();
    logger.info("待发布事件数量: " + events.size());
    if (events != null && !events.isEmpty()) {
      for (EventPublish event : events) {
        try {
          //eventActivator.sendMessage抛异常不会导致整个事务回滚
          if (eventActivator.sendMessage(event.getPayload(), event.getEventType())) {
            event.setStatus(ProcessStatus.PROCESSED.name());
            saveEventPublish(event);
          }
        } catch (EventException e) {
          logger.error(e.getMessage());
        } catch (Exception e) {
          logger.error(String.format("发送消息到队列的时候发生异常, EventPublish[id=%d, payload=%s]",
                                     event.getId(), event.getPayload()), e);
        }

      }
    }
  }

  private void saveEventPublish(EventPublish eventPublish) {

    if (eventPublish instanceof NotifyEventPublish) {
      notifyEventPublishMapper.updateByPrimaryKey((NotifyEventPublish) eventPublish);
    } else if (eventPublish instanceof AskRequestEventPublish) {
      askRequestEventPublishMapper.updateByPrimaryKey((AskRequestEventPublish) eventPublish);
    } else if (eventPublish instanceof AskResponseEventPublish) {
      askResponseEventPublishMapper.updateByPrimaryKey((AskResponseEventPublish) eventPublish);
    } else if (eventPublish instanceof RevokeAskEventPublish) {
      revokeAskEventPublishMapper.updateByPrimaryKey((RevokeAskEventPublish) eventPublish);
    } else {
      throw new EventException(String.format("unknown eventPublish class: %s, id: %d",
                                             eventPublish.getClass(), eventPublish.getId()));
    }
  }


  @Transactional
  public void searchAndHandleUnprocessedEvent() {

    List<EventProcess> events = eventProcessMapper.findByStatus(ProcessStatus.NEW.name());
//        logger.info("待处理事件数量: " + events.size());
    CountDownLatch latch = new CountDownLatch(events.size());

    for (EventProcess event : events) {
      final Long eventProcessId = event.getId();
      taskExecutor.execute(() -> {
        try {
//          EventBus eventBus = ApplicationContextHolder.context.getBean(EventBus.class);
          //handleEventProcess方法内报异常只回滚内部事务
          handleEventProcess(eventProcessId)
              .map(eventWatchProcess -> eventWatchService.addToQueue(eventWatchProcess));
        } catch (EventException e) {
          logger.error(e.getMessage());
        } catch (Exception e) {
          logger.error(String.format("处理事件的时候发生异常, EventProcess[id=%d]",
                                     eventProcessId), e);
        } finally {
          latch.countDown();
        }
      });
    }

    try {
      //等待事件异步处理完成
      latch.await();
    } catch (InterruptedException e) {
      logger.error("", e);
    }

  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public Optional<EventWatchProcess> handleEventProcess(Long eventProcessId) {

    Optional<EventWatchProcess> eventWatchProcessOptional = Optional.empty();

    EventProcess eventProcess = eventProcessMapper.selectByPrimaryKey(eventProcessId);
    if (!eventProcess.getStatus().equals(ProcessStatus.NEW.name())) {
      //已经被处理过了, 忽略
      return eventWatchProcessOptional;
    }

    logger.debug(String.format("handle event process, id: %d, event category: %s ",
                               eventProcessId, eventProcess.getEventCategory()));

    switch (EventCategory.valueOf(eventProcess.getEventCategory())) {
      case NOTIFY:
        processNotifyEvent(eventProcess);
        break;
      case ASK:
        processAskEvent(eventProcess);
        break;
      case REVOKE:
        processRevokeEvent(eventProcess);
        break;
      case ASKRESP:
        eventWatchProcessOptional = processAskResponseEvent(eventProcess);
        break;
      default:
        throw new EventException(
            String.format("unknown event category, process id: %d, event category: %s ",
                          eventProcessId, eventProcess.getEventCategory()));
    }

    eventProcess.setStatus(ProcessStatus.PROCESSED.name());
    eventProcessMapper.updateByPrimaryKey(eventProcess);

    return eventWatchProcessOptional;
  }


  private void processNotifyEvent(EventProcess event) {

    String eventType = event.getEventType();

    EventType type = EventType.valueOfIgnoreCase(eventType);

    Set<NotifyEventHandler> eventHandlers = eventRegistry.getNotifyEventHandlers(type);
    if (eventHandlers == null || eventHandlers.isEmpty()) {
      logger.error(String.format("EventProcess[id=%d, type=%s, payload=%s]的eventHandlers列表为空'",
                                 event.getId(), type, event.getPayload()));
      return;
    }

    NotifyEvent
        notifyEvent =
        (NotifyEvent) eventRegistry.deserializeEvent(type, event.getPayload());

    eventHandlers.forEach(
        handler -> executeEventHandler(
            event.getId(),
            () -> {
              handler.notify(notifyEvent);
              return null;
            },
            null));

  }


  private void processAskEvent(EventProcess event) {

    String eventType = event.getEventType();

    EventType type = EventType.valueOfIgnoreCase(eventType);

    Set<AskEventHandler> eventHandlers = eventRegistry.getAskEventHandlers(type);
    if (eventHandlers == null || eventHandlers.isEmpty()) {
      logger.error(String.format("EventProcess[id=%d, type=%s, payload=%s]的eventHandlers列表为空'",
                                 event.getId(), type, event.getPayload()));
      return;
    }

    AskEvent askEvent = (AskEvent) eventRegistry.deserializeEvent(type, event.getPayload());

    eventHandlers.forEach(handler -> {
      EventHandlerResponse<BooleanWrapper> result = executeEventHandler(event.getId(),
                                                                        () -> handler
                                                                            .processRequest(
                                                                                askEvent),
                                                                        new BooleanWrapper(false));
      createAskResponse(askEvent, result.getValue());
    });

  }

  private void processRevokeEvent(EventProcess event) {

    RevokeAskEvent revokeAskEvent = (RevokeAskEvent) eventRegistry.deserializeEvent(
        EventType.REVOKE_ASK,
        event.getPayload());

    EventProcess
        askEventProcess =
        eventProcessMapper.selectByEventId(revokeAskEvent.getAskEventId());
    if (askEventProcess == null) {
      return;
    }

    EventType type = EventType.valueOf(askEventProcess.getEventType());
    Set<RevokableAskEventHandler> eventHandlers = eventRegistry.getRevokableAskEventHandlers(type);
    if (eventHandlers == null || eventHandlers.isEmpty()) {
      logger.error(String.format("EventProcess[id=%d, type=%s, payload=%s]的eventHandlers列表为空'",
                                 askEventProcess.getId(), type, askEventProcess.getPayload()));
      return;
    }
    if (askResponseEventPublishMapper
        .findByAskEventId(askEventProcess.getEventId())) {//如果ask_event 执行成功才进行revoke
      AskEvent
          originEvent =
          (AskEvent) eventRegistry.deserializeEvent(type, askEventProcess.getPayload());

      eventHandlers.forEach(
          handler -> executeEventHandler(
              event.getId(),
              () -> {
                handler.processRevoke(originEvent, revokeAskEvent.getFailureInfo());
                return null;
              },
              null
          )
      );
    }
  }

  private Optional<EventWatchProcess> processAskResponseEvent(EventProcess event) {

    AskResponseEvent
        askResponseEvent =
        eventRegistry.deserializeAskResponseEvent(event.getPayload());
    Long askEventId = askResponseEvent.getAskEventId();
    AskRequestEventPublish
        askRequestEventPublish =
        eventPublishService.getAskRequestEventByEventId(askEventId);
    if (askRequestEventPublish == null || !askRequestEventPublish.getAskEventStatus()
        .equals(AskEventStatus.PENDING.name())) {
      return Optional.empty();
    }

    AskEventStatus askEventStatus;
    FailureInfo failureInfo = null;
    if (askResponseEvent.isSuccess()) {
      askEventStatus = AskEventStatus.SUCCESS;
    } else {
      askEventStatus = AskEventStatus.FAILED;
      failureInfo = new FailureInfo(FailureReason.FAILED, new Date());
    }
    askRequestEventPublish.setAskEventStatus(askEventStatus.name());
    askRequestEventPublishMapper.updateByPrimaryKey(askRequestEventPublish);

    return eventWatchService
        .processEventWatch(askRequestEventPublish.getWatchId(), askEventStatus, failureInfo);

  }

  /**
   * 发送ask结果
   */
  private AskResponseEventPublish createAskResponse(AskEvent askEvent, BooleanWrapper result) {
    AskResponseEvent
        askResponseEvent =
        new AskResponseEvent(result.isSuccess(), result.getMessage(), askEvent.getId());
    fillEventId(askResponseEvent);
    AskResponseEventPublish eventPublish = new AskResponseEventPublish();
    eventPublish.setSuccess(result.isSuccess());
    eventPublish.setAskEventId(askEvent.getId());
    eventPublish.setEventType(AskResponseEvent.EVENT_TYPE);
    eventPublish.setEventId(askResponseEvent.getId());
    eventPublish.setPayload(EventUtils.serializeEvent(askResponseEvent));

    askResponseEventPublishMapper.insert(eventPublish);

    return eventPublish;
  }


  private <T> EventHandlerResponse<T> executeEventHandler(Long eventProcessId, Supplier<T> supplier,
                                                          T defaultValue) {

    T value = defaultValue;
    String errorMessage = null;
    Stopwatch stopwatch = null;
    try {
      if (logger.isDebugEnabled()) {
        stopwatch = Stopwatch.createStarted();
      }
      //开启新事务, 防止handler执行方法报错导致整体事务回滚
      value = eventHandlerExecutor.executeEventHandler(supplier);
    } catch (TransactionSystemException ignore) {

    } catch (AppBusinessException e) {
      errorMessage = e.getMessage();
    } catch (Exception e) {
      logger.error("", e);
      errorMessage = e.getMessage();
    } finally {
      if (logger.isDebugEnabled() && stopwatch != null) {
        stopwatch.stop();
        logger.debug(String.format("执行事件回调结束耗时%dms, EventProcess[id=%d]",
                                   stopwatch.elapsed(TimeUnit.MILLISECONDS), eventProcessId));
      }
    }

    return new EventHandlerResponse<>(value, errorMessage);
  }


  @Transactional
  public EventProcess recordEvent(String message) {
    Map<String, Object> eventMap = EventUtils.retrieveEventMapFromJson(message);
    EventType eventType = EventType.valueOfIgnoreCase((String) eventMap.get("type"));
    EventCategory eventCategory = eventRegistry.getEventCategoryByType(eventType);
    if (eventCategory.equals(EventCategory.ASKRESP.name()) || eventCategory
        .equals(EventCategory.REVOKE.name())) {
      Long askEventId = (Long) eventMap.get("askEventId");
      if (askEventId == null) {
        throw new EventException(
            "EventCategory为ASKRESP或REVOKE的事件, askEventId为null, payload: " + message);
      }
      boolean eventPublishNotExist = true;
      if (eventCategory.equals(EventCategory.ASKRESP.name())) {
        eventPublishNotExist = askRequestEventPublishMapper.getByEventId(askEventId) == null;
      } else if (eventCategory.equals(EventCategory.REVOKE.name())) {
        eventPublishNotExist =
            askResponseEventPublishMapper.countByAskEventId(askEventId) == 0L;
      }
      if (eventPublishNotExist) {
        //如果为ASKRESP或REVOKE事件并且请求id在数据库不存在, 则忽略这个事件
        return null;
      }
    }
    if (logger.isDebugEnabled()) {
      logger.debug("receive message from kafka: {}", message);
    }
    if (eventProcessMapper.selectByEventIdAndEventTypeAndConsumerGroup((Long) eventMap.get("id"),
                                                                       eventType.name(),
                                                                       applicationConstant.groupName)
        == null) {
      EventProcess eventProcess = new EventProcess();
      eventProcess.setPayload(message);
      eventProcess.setEventId((Long) eventMap.get("id"));
      eventProcess.setEventType(eventType.name());
      eventProcess.setEventCategory(eventCategory.name());
      eventProcess.setGroupName(applicationConstant.groupName);
      eventProcessMapper.insert(eventProcess);
      return eventProcess;
    }
    return null;
  }

  //不在这里加事务注解, 因为想让这个方法内对service的调用都是独立事务.
  public void handleUnprocessedEventWatchProcess() {
    List<EventWatchProcess>
        eventWatchProcessList =
        eventWatchService.findUnprocessedEventWatchProcess();
//        logger.info("待处理eventWatchProcess数量: " + eventWatchProcessList.size());
    Set<Long> successIdSet = new HashSet<>();
    Set<Long> watchIdSet = new HashSet<>();
    for (EventWatchProcess eventWatchProcess : eventWatchProcessList) {
      try {
        if (watchIdSet.add(eventWatchProcess.getWatchId())) {
          //processUnitedEventWatch方法内报异常只回滚内部事务
          eventWatchService.processUnitedEventWatch(eventWatchProcess);
        }
        successIdSet.add(eventWatchProcess.getId());
      } catch (EventException e) {
        logger.error(e.getMessage(), e);
        eventWatchService.addToQueue(eventWatchProcess);
        watchIdSet.remove(eventWatchProcess.getWatchId());
      } catch (Exception e) {
        logger
            .error("处理unitedEventWatch事件的时候发生异常, EventWatchProcessId:" + eventWatchProcess.getId(),
                   e);
        eventWatchService.addToQueue(eventWatchProcess);
        watchIdSet.remove(eventWatchProcess.getWatchId());
      }
    }

    if (!successIdSet.isEmpty()) {
      eventWatchService
          .updateStatusBatchToProcessed(successIdSet.toArray(new Long[successIdSet.size()]));
    }
  }

  //不在这里加事务注解, 因为想让这个方法内对service的调用都是独立事务.
  public void handleTimeoutEventWatch() {
    Date now = new Date();
    List<EventWatch> eventWatchList = eventWatchService.findTimeoutEventWatch(now);
    FailureInfo failureInfo = new FailureInfo(FailureReason.TIMEOUT, now);
    if (eventWatchList != null && !eventWatchList.isEmpty()) {
      for (EventWatch eventWatch : eventWatchList) {
        try {
          eventWatchService
              .processEventWatch(eventWatch.getId(), AskEventStatus.TIMEOUT, failureInfo)
              .map(eventWatchProcess -> eventWatchService.addToQueue(eventWatchProcess));
        } catch (EventException e) {
          logger.error(e.getMessage());
        } catch (Exception e) {
          logger.error(String.format("处理超时EventWatch的时候发生异常, id=%d",
                                     eventWatch.getId()), e);
        }
      }
    }
  }


  public Long generateEventId() {
    //TODO generate id
    return Long.parseLong(RandomStringUtils.randomNumeric(18));
  }

  public void fillEventId(BaseEvent baseEvent) {
    if (baseEvent.getId() != null) {
      throw new EventException("event id不为空, id:" + baseEvent.getId());
    }
    baseEvent.setId(generateEventId());

  }


  public void setEventActivator(EventActivator eventActivator) {
    this.eventActivator = eventActivator;
  }


  private static class EventHandlerResponse<T> {

    private T value;

    String errorMessage;

    public EventHandlerResponse(T value, String errorMessage) {
      this.value = value;
      this.errorMessage = errorMessage;
    }

    public T getValue() {
      return value;
    }

    public String getErrorMessage() {
      return errorMessage;
    }
  }
}
