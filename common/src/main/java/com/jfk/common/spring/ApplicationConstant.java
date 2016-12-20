package com.jfk.common.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liubin on 2016/4/20.
 */
@Configuration
public class ApplicationConstant {

    @Value("${server.zkAddress}")
    public String zkAddress;

    @Value("${spring.application.name}")
    public String applicationName;

    @Value("${server.group}")
    public String groupName;

    @Bean
    public ApplicationContextHolder applicationContextHolder() {
        return ApplicationContextHolder.getInstance();
    }



//    @Value("${spring.application.index}")
//    public int applicationIndex;

}
