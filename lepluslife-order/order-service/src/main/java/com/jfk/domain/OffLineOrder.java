package com.jfk.domain;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.util.Date;
import java.util.Random;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import tk.mybatis.mapper.annotation.ColumnType;

/**
 * Created by wcg on 2016/12/2.
 */
public class OffLineOrder {

  @Id
  @GeneratedValue(generator = "JDBC")
  private Long id;

  @ColumnType(jdbcType = JdbcType.VARCHAR)
  private String orderSid;

  private Long userId;

  private Long totalPrice;

  @ColumnType(jdbcType = JdbcType.DATETIMEOFFSET)
  private Date createDate = new Date();

  @ColumnType(jdbcType = JdbcType.DATETIMEOFFSET)
  private Date completeDate;

  private Long orderStatus;//0 未支付 1 已支付 2 支付失败

  private Long version = 0L;

  public Long getOrderStatus() {
    return orderStatus;
  }

  public void setOrderStatus(Long orderStatus) {
    this.orderStatus = orderStatus;
  }

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

  public String getOrderSid() {
    return orderSid;
  }

  public void setOrderSid(String orderSid) {
    this.orderSid = orderSid;
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
