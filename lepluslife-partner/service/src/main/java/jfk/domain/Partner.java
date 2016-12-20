package jfk.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by wcg on 2016/12/15.
 */
public class Partner {

  @Id
  @GeneratedValue(generator = "JDBC")
  private Long id;

  private String name;

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
