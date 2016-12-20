package com.jfk.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by wcg on 2016/11/13.
 */
public class User {

  @Id
  @GeneratedValue(generator = "JDBC")
  private Long id;

  private String userName;

  private Long version = 0L;

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
}
