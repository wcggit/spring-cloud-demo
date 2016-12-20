//package com.jfk.config;
//
//import com.netflix.loadbalancer.Server;
//
//import org.springframework.cloud.netflix.ribbon.StaticServerList;
//import org.springframework.context.annotation.Bean;
//
///**
// * Created by wcg on 2016/12/6.
// */
//public class AtlasClientConfiguration {
//
//  @Bean
//  public StaticServerList<Server> ribbonServerList() {
//    return new StaticServerList<Server>(new Server("atlas", 7101));
//  }
//
//}
