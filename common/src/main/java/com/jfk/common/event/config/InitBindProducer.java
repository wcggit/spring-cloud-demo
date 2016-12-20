package com.jfk.common.event.config;


import com.jfk.base.event.constants.EventType;
import com.jfk.base.event.domain.AskResponseEvent;
import com.jfk.base.event.domain.RevokeAskEvent;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by liubin on 2016/6/28.
 */
public class InitBindProducer implements InitializingBean {

    @Autowired
    private BinderAwareChannelResolver binderAwareChannelResolver;

    private Set<EventType> preInitializeProducers = new HashSet<>();

    public InitBindProducer() {
        preInitializeProducers.add(EventType.valueOf(AskResponseEvent.EVENT_TYPE));
        preInitializeProducers.add(EventType.valueOf(RevokeAskEvent.EVENT_TYPE));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        preInitializeProducers.stream().forEach(x -> binderAwareChannelResolver.resolveDestination(x.name()));
    }

    public void addPreInitializeProducers(EventType eventType) {
        preInitializeProducers.add(eventType);
    }

}
