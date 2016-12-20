package jfk.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wcg on 2016/11/29.
 */
@Configuration
public class BaseConfig {

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }


}
