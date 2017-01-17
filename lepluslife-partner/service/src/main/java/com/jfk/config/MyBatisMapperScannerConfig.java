package com.jfk.config;

/**
* Created by wcg on 2016/11/14.
*/

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

import javax.inject.Inject;

import tk.mybatis.spring.mapper.MapperScannerConfigurer;

/**
* MyBatis扫描接口，使用的tk.mybatis.spring.mapper.MapperScannerConfigurer，如果你不使用通用Mapper，可以改为org.xxx...
*
* @author liuzh
* @since 2015-12-19 14:46
*/
@Configuration
//TODO 注意，由于MapperScannerConfigurer执行的比较早，所以必须有下面的注解
@AutoConfigureBefore(MybatisConfig.class)
public class MyBatisMapperScannerConfig {

  @Inject
  private SqlSessionFactory sqlSessionFactory;

  @Bean
  public MapperScannerConfigurer mapperScannerConfigurer() {
    MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
    mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
    mapperScannerConfigurer.setBasePackage("com.jfk.mapper,com.jfk.common");
    Properties properties = new Properties();
//    properties.setProperty("mappers", "tk.mybatis.springboot.util.MyMapper");
    properties.setProperty("notEmpty", "false");
    properties.setProperty("IDENTITY", "MYSQL");
    mapperScannerConfigurer.setProperties(properties);
    return mapperScannerConfigurer;
  }

}

