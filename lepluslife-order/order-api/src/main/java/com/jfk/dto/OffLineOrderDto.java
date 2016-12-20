package com.jfk.dto;

import java.util.Date;

/**
 * Created by wcg on 2016/12/2.
 */
public class OffLineOrderDto {

  private Long id;

  private String sid;

  private Long userId;

  private Long totalPrice;

  private Date createDate;

  private Date completeDate;

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

  public String getSid() {
    return sid;
  }

  public void setSid(String sid) {
    this.sid = sid;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(Long totalPrice) {
    this.totalPrice = totalPrice;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getCompleteDate() {
    return completeDate;
  }

  public void setCompleteDate(Date completeDate) {
    this.completeDate = completeDate;
  }
}
