package com.jfk.common.event.domain;


import com.jfk.common.domain.VersionEntity;
import com.jfk.common.event.constant.ProcessStatus;

import org.apache.ibatis.type.JdbcType;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import tk.mybatis.mapper.annotation.ColumnType;

/**
 * Created by liubin on 2016/3/28.
 */
public class EventProcess extends VersionEntity {

  @Id
  @GeneratedValue(generator = "JDBC")
  private Long id;

  @ColumnType(jdbcType = JdbcType.VARCHAR)
  private String payload;

  @ColumnType(jdbcType = JdbcType.VARCHAR)
  private String status = ProcessStatus.NEW.name();

  @ColumnType(jdbcType = JdbcType.VARCHAR)
  private String eventCategory;

  private Long eventId;

  @ColumnType(jdbcType = JdbcType.VARCHAR)
  private String eventType;

  @ColumnType(jdbcType = JdbcType.VARCHAR)
  private String groupName;

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getEventCategory() {
    return eventCategory;
  }

  public void setEventCategory(String eventCategory) {
    this.eventCategory = eventCategory;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  public Long getEventId() {
    return eventId;
  }

  public void setEventId(Long eventId) {
    this.eventId = eventId;
  }

  public String getEventType() {
    return eventType;
  }

  @Override
  public String toString() {
    return "EventProcess{" +
           "id=" + id +
           ", payload='" + payload + '\'' +
           ", status=" + status +
           ", eventCategory=" + eventCategory +
           ", eventId=" + eventId +
           ", eventType=" + eventType +
           "} " + super.toString();
  }
}
