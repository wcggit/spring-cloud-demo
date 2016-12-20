package com.jfk.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;

/**
 * Created by wcg on 2016/11/29.
 */
@Configuration
public class FooConfiguration {

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }


  @Bean
  Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }

}
