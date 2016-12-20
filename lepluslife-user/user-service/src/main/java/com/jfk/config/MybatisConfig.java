package com.jfk.config;

import com.github.pagehelper.PageHelper;
import com.mook.locker.interceptor.OptimisticLocker;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


/**
 * Created by wcg on 2016/11/13.
 */
@Configuration
@AutoConfigureBefore(MybatisConfig.class)
public class MybatisConfig {

  @Bean
  public PageHelper pageHelper() {
    PageHelper pageHelper = new PageHelper();
    Properties p = new Properties();
    p.setProperty("offsetAsPageNum", "true");
    p.setProperty("rowBoundsWithCount", "true");
    p.setProperty("reasonable", "true");
    pageHelper.setProperties(p);
    return pageHelper;
  }


  @Bean
  public OptimisticLocker optimisticLocker() {
    return new OptimisticLocker();
  }


}