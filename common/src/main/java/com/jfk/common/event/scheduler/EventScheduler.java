package com.jfk.common.event.scheduler;

import com.jfk.common.event.service.EventBus;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by liubin on 2016/4/19.
 */
public class EventScheduler{

    EventBus eventBus;

    public EventScheduler(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Scheduled(fixedRate = 10000L)
    public void sendUnpublishedEvent() {
        eventBus.sendUnpublishedEvent();
    }

    @Scheduled(fixedRate = 10000L)
    public void searchAndHandleUnprocessedEvent() {
        eventBus.searchAndHandleUnprocessedEvent();
    }

    @Scheduled(fixedRate = 10000L)
    public void handleUnprocessedEventWatchProcess() {
        eventBus.handleUnprocessedEventWatchProcess();
    }

    @Scheduled(fixedRate = 10000L)
    public void handleTimeoutEventWatch() {
        eventBus.handleTimeoutEventWatch();
    }



}
