package com.jfk.common.scheduler.config;


import com.jfk.common.event.scheduler.EventScheduler;
import com.jfk.common.event.service.EventBus;
import com.jfk.common.scheduler.ZkCoordinateScheduledExecutor;
import com.jfk.common.scheduler.ZkSchedulerCoordinator;
import com.jfk.common.spring.ApplicationConstant;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Created by liubin on 2016/4/8.
 */
@EnableScheduling
@Configuration
public class SchedulerConfiguration implements SchedulingConfigurer{


    @Bean
    public ZkSchedulerCoordinator zkSchedulerCoordinator(ApplicationConstant applicationConstant){

        return new ZkSchedulerCoordinator(applicationConstant);

    }

    @Bean
    public EventScheduler eventScheduler(EventBus eventBus) {
        return new EventScheduler(eventBus);
    }


    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        TaskScheduler taskScheduler = new ConcurrentTaskScheduler(new ZkCoordinateScheduledExecutor(5));
        taskRegistrar.setTaskScheduler(taskScheduler);

    }


}
