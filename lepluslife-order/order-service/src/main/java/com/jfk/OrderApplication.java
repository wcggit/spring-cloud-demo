package com.jfk;

import com.jfk.base.constant.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.client.ConfigClientProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by wcg on 2016/11/13.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.jfk.client"})
@EnableScheduling
public class OrderApplication {

  private static final Logger log = LoggerFactory.getLogger(OrderApplication.class);

  @Inject
  private Environment env;


//
//  @Bean
//  public ConfigClientProperties configClientProperties() {
//    ConfigClientProperties client = new ConfigClientProperties(this.env);
//    Collection<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
//    if (activeProfiles
//        .contains(Constants.SPRING_PROFILE_DEVELOPMENT)) {
//      client.setEnabled(false);
//    }
//    return client;
//  }


  /**
   * Initializes myseek. <p/> Spring profiles can be configured with a program arguments
   * --spring.profiles.active=your-active-profile <p/> <p> You can find more information on how
   * profiles work with JHipster on <a href="http://jhipster.github.io/profiles.html">http://jhipster.github.io/profiles.html</a>.
   * </p>
   */
  @PostConstruct
  public void initApplication() throws IOException {
    if (env.getActiveProfiles().length == 0) {
      log.warn("No Spring profile configured, running with default configuration");
    } else {
      log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
    }
  }

  public static void main(String[] args) throws UnknownHostException {
    SpringApplication app = new SpringApplication(OrderApplication.class);
    SimpleCommandLinePropertySource source = new SimpleCommandLinePropertySource(args);
    addDefaultProfile(app, source);
    app.run(args);

  }

  /**
   * If no profile has been configured, set by default the "dev" profile.
   */
  private static void addDefaultProfile(SpringApplication app,
                                        SimpleCommandLinePropertySource source) {
    if (!source.containsProperty("spring.profiles.active") &&
        !System.getenv().containsKey("SPRING_PROFILES_ACTIVE")) {
      app.setAdditionalProfiles(Constants.SPRING_PROFILE_DEVELOPMENT);
    }
  }

}
