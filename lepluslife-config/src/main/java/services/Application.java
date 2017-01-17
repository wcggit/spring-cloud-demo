package services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.SimpleCommandLinePropertySource;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.annotation.PostConstruct;

@Configuration
@EnableAutoConfiguration
@EnableDiscoveryClient
@EnableConfigServer
public class Application {

  private static final Logger log = LoggerFactory.getLogger(Application.class);

  @Autowired
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
    SpringApplication app = new SpringApplication(Application.class);
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
      app.setAdditionalProfiles("dev");
    }
  }
}
