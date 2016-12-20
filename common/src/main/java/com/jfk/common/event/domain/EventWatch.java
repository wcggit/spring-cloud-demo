package com.jfk.common.event.domain;


import com.jfk.common.domain.VersionEntity;
import com.jfk.common.event.constant.AskEventStatus;

import org.apache.ibatis.type.JdbcType;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import tk.mybatis.mapper.annotation.ColumnType;

/**
 * Created by liubin on 2016/3/28.
 */
public class EventWatch extends VersionEntity {

  @Id
  @GeneratedValue(generator = "JDBC")
  private Long id;

  @ColumnType(jdbcType = JdbcType.VARCHAR)
  private String askEventStatus;

  @ColumnType(jdbcType = JdbcType.VARCHAR)
  private String extraParams;

  @ColumnType(jdbcType = JdbcType.VARCHAR)
  private String askEventIds;

  @ColumnType(jdbcType = JdbcType.VARCHAR)
  private String callbackClass;

  @ColumnType(jdbcType = JdbcType.BOOLEAN)
  private boolean united;

  @ColumnType(jdbcType = JdbcType.DATETIMEOFFSET)
  private Date timeoutTime;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAskEventStatus() {
    return askEventStatus;
  }

  public void setAskEventStatus(String askEventStatus) {
    this.askEventStatus = askEventStatus;
  }

  public void setAskEventIds(String askEventIds) {
    this.askEventIds = askEventIds;
  }

  public void setTimeoutTime(Date timeoutTime) {
    this.timeoutTime = timeoutTime;
  }

  public String getExtraParams() {
    return extraParams;
  }

  public void setExtraParams(String extraParams) {
    this.extraParams = extraParams;
  }

  public List<Long> getAskEventIdsList() {
    return Arrays.asList(askEventIds.split(",")).stream()
        .map(Long::parseLong)
        .collect(Collectors.toList());
  }

  public String getAskEventIds() {
    return askEventIds;
  }

  public void setAskEventIds(List<Long> askEventIds) {
    this.askEventIds = String.join(",",
                                   askEventIds.stream().map(String::valueOf)
                                       .collect(Collectors.toList()));
  }

  public String getCallbackClass() {
    return callbackClass;
  }

  public void setCallbackClass(String callbackClass) {
    this.callbackClass = callbackClass;
  }

  public boolean isUnited() {
    return united;
  }

  public void setUnited(boolean united) {
    this.united = united;
  }

  public Date getTimeoutTime() {
    return timeoutTime;
  }
}
