package com.jfk;

import com.jfk.distribute.transaction.DistributeTransactionManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Import;

/**
 * Created by wcg on 2016/12/2.
 */
@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@EnableHystrix
@Import(value = {DistributeTransactionManager.class})
public class PayApplication {


  public static void main(String[] args) {
    SpringApplication.run(PayApplication.class, args);
  }


}
