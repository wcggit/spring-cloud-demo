package com.jfk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by wcg on 2016/11/13.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.jfk.client"})
@EnableScheduling
public class OrderApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrderApplication.class, args);
  }

}
