//package com.jfk.config;
//
///**
// * Created by wcg on 2016/12/26.
// */
//
//import com.jfk.base.constant.Constants;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Collection;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.config.client.ConfigClientProperties;
//import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.annotation.Order;
//import org.springframework.core.env.Environment;
//import org.springframework.core.env.PropertySource;
//import org.springframework.http.HttpRequest;
//import org.springframework.http.client.ClientHttpRequestExecution;
//import org.springframework.http.client.ClientHttpRequestInterceptor;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.http.client.SimpleClientHttpRequestFactory;
//import org.springframework.web.client.RestTemplate;
//
//@Order(0)
//@Configuration
//public class ConfigSourceLocator extends ConfigServicePropertySourceLocator {
//
//  @Autowired
//  private Environment environment;
//
//  public ConfigSourceLocator(ConfigClientProperties defaults) {
//    super(defaults);
//  }
//
//  @Override
//  public PropertySource<?> locate(Environment environment) {
//    Collection<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
//    if (activeProfiles
//        .contains(Constants.SPRING_PROFILE_DEVELOPMENT)) {
//      return null;
//    }
//    return super.locate(environment);
//  }
//}