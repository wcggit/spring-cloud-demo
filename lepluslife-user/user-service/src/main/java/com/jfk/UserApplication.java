package com.jfk;

import com.jfk.common.event.config.EventConfiguration;
import com.jfk.common.scheduler.config.SchedulerConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by wcg on 2016/11/13.
 */
@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@EnableScheduling
@Import({ EventConfiguration.class, SchedulerConfiguration.class})
public class UserApplication {

  public static void main(String[] args) {
    SpringApplication.run(UserApplication.class, args);
  }

}
