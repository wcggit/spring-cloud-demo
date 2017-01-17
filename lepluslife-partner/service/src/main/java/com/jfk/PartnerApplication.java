package com.jfk;

import com.jfk.base.constant.Constants;
import com.jfk.common.event.config.EventConfiguration;
import com.jfk.common.scheduler.config.SchedulerConfiguration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by wcg on 2016/11/13.
 */
@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@EnableScheduling
@Import({ EventConfiguration.class, SchedulerConfiguration.class})
public class PartnerApplication {

  private static final Logger log = LoggerFactory.getLogger(PartnerApplication.class);

  @Inject
  private Environment env;

  @PostConstruct
  public void initApplication() throws IOException {
    if (env.getActiveProfiles().length == 0) {
      log.warn("No Spring profile configured, running with default configuration");
    } else {
      log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
    }
  }

  public static void main(String[] args) throws UnknownHostException {
    SpringApplication app = new SpringApplication(PartnerApplication.class);
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
