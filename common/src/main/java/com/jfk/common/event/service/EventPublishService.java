package com.jfk.common.event.service;

import com.jfk.common.event.constant.ProcessStatus;
import com.jfk.common.event.domain.AskRequestEventPublish;
import com.jfk.common.event.domain.EventPublish;
import com.jfk.common.event.mapper.AskRequestEventPublishMapper;
import com.jfk.common.event.mapper.AskResponseEventPublishMapper;
import com.jfk.common.event.mapper.NotifyEventPublishMapper;
import com.jfk.common.event.mapper.RevokeAskEventPublishMapper;
import com.jfk.common.exception.EventException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.text.html.Option;

/**
 * Created by liubin on 2016/6/13.
 */
@Service
public class EventPublishService {

  @Autowired
  protected NotifyEventPublishMapper notifyEventPublishRepository;

  @Autowired
  protected AskRequestEventPublishMapper askRequestEventPublishRepository;

  @Autowired
  protected RevokeAskEventPublishMapper revokeAskEventPublishRepository;

  @Autowired
  protected AskResponseEventPublishMapper askResponseEventPublishRepository;


  @Transactional(readOnly = true)
  public List<EventPublish> findUnpublishedEvent() {
    List<EventPublish> unpublishedEvents = new ArrayList<>();
    Optional.ofNullable(notifyEventPublishRepository.findByStatus(ProcessStatus.NEW.name()))
        .map(
            list -> list.isEmpty() ? 0 : unpublishedEvents.addAll(list));
    Optional.ofNullable(askRequestEventPublishRepository.findByStatus(ProcessStatus.NEW.name()))
        .map(
            list -> list.isEmpty() ? 0 : unpublishedEvents.addAll(list));
    Optional.ofNullable(revokeAskEventPublishRepository.findByStatus(ProcessStatus.NEW.name()))
        .map(
            list -> list.isEmpty() ? 0 : unpublishedEvents.addAll(list));
    Optional
        .ofNullable(askResponseEventPublishRepository.findByStatus(ProcessStatus.NEW.name()))
        .map(
            list -> list.isEmpty() ? 0 : unpublishedEvents.addAll(list));
    return unpublishedEvents;
  }

  @Transactional(readOnly = true)
  public AskRequestEventPublish getAskRequestEventByEventId(Long eventId) {
    AskRequestEventPublish
        askRequestEventPublish =
        askRequestEventPublishRepository.getByEventId(eventId);

    return askRequestEventPublish;
  }

  @Transactional(readOnly = true)
  public List<AskRequestEventPublish> findAskRequestEventByEventId(List<Long> eventIds) {
    Map<Long, AskRequestEventPublish>
        map =
        askRequestEventPublishRepository.findAllByEventIdIn(eventIds)
            .stream()
            .collect(Collectors.toMap(AskRequestEventPublish::getEventId, Function.identity()));

    Set<Long> eventNotExistIdSet = new HashSet<>();

    List<AskRequestEventPublish> askRequestEventPublishList = eventIds.stream().map(eventId -> {
      AskRequestEventPublish p = map.get(eventId);
      if (p == null) {
        eventNotExistIdSet.add(eventId);
      }
      return p;
    }).collect(Collectors.toList());

    if (!eventNotExistIdSet.isEmpty()) {
      throw new EventException(
          String.format("根据事件ID[%s]没有找到AskRequestEventPublish", eventNotExistIdSet));
    }

    return askRequestEventPublishList;
  }


}
