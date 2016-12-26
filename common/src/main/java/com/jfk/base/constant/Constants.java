package com.jfk.base.constant;

/**
 * Created by wcg on 2016/12/26.
 */
public final class Constants {

  // Spring profile for development, production and "fast", see http://jhipster.github.io/profiles.html
  public static final String SPRING_PROFILE_DEVELOPMENT = "dev";

  public static final String SPRING_PROFILE_PRODUCTION = "prod";
  // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
  public static final String SPRING_PROFILE_CLOUD = "cloud";

  private Constants() {
  }
}
