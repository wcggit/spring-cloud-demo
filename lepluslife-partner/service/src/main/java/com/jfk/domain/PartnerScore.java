package com.jfk.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by wcg on 2016/12/15.
 */
public class PartnerScore {

  @Id
  @GeneratedValue(generator = "JDBC")
  private Long id;

  private Long partnerId;

  private Long score;

  private Long version = 0L;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPartnerId() {
    return partnerId;
  }

  public void setPartnerId(Long partnerId) {
    this.partnerId = partnerId;
  }

  public Long getScore() {
    return score;
  }

  public void setScore(Long score) {
    this.score = score;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

}
