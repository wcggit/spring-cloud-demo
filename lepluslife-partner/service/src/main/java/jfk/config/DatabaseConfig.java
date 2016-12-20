package jfk.config;

/**
 * Created by wcg on 2016/11/13.
 */

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement()
public class DatabaseConfig {


  @Bean(destroyMethod = "close")
  public DataSource dataSource(DataSourceProperties dataSourceProperties) {
    HikariConfig config = new HikariConfig();
    config.setDataSourceClassName(dataSourceProperties.getDriverClassName());
    config.addDataSourceProperty("url", dataSourceProperties.getUrl());
    if (dataSourceProperties.getUsername() != null) {
      config.addDataSourceProperty("user", dataSourceProperties.getUsername());
    } else {
      config.addDataSourceProperty("user", ""); // HikariCP doesn't allow null user
    }
    if (dataSourceProperties.getPassword() != null) {
      config.addDataSourceProperty("password", dataSourceProperties.getPassword());
    } else {
      config.addDataSourceProperty("password", ""); // HikariCP doesn't allow null password
    }
    config.setMaximumPoolSize(30);

    return new HikariDataSource(config);
  }

}
