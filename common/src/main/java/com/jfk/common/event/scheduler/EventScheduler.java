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

    @Scheduled(fixedDelayString = "${server.event.delay}" )
    public void sendUnpublishedEvent() {
        eventBus.sendUnpublishedEvent();
    }

    @Scheduled(fixedDelayString = "${server.event.delay}")
    public void searchAndHandleUnprocessedEvent() {
        eventBus.searchAndHandleUnprocessedEvent();
    }

    @Scheduled(fixedDelayString = "${server.event.delay}")
    public void handleUnprocessedEventWatchProcess() {
        eventBus.handleUnprocessedEventWatchProcess();
    }

    @Scheduled(fixedDelayString = "${server.event.delay}")
    public void handleTimeoutEventWatch() {
        eventBus.handleTimeoutEventWatch();
    }



}
