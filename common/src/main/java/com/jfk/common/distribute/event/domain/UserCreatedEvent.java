package com.jfk.common.distribute.event.domain;

import com.jfk.base.event.constants.EventType;
import com.jfk.base.event.domain.NotifyEvent;
import com.jfk.common.event.constant.AskEventStatus;

/**
 * Created by wcg on 2016/12/8.
 */
public class UserCreatedEvent extends NotifyEvent {

  public static final String EVENT_TYPE = EventType.USER_CREATED.name();

  public Long userId;

  private String name;

  public UserCreatedEvent() {
  }

  public UserCreatedEvent(Long userId, String name) {
    this.userId = userId;
    this.name = name;
  }

  @Override
  public String getType() {
    return EVENT_TYPE;
  }

  public static String getEventType() {
    return EVENT_TYPE;
  }

  @Override
  public String toString() {
    return "UserCreatedEvent{} " + super.toString();
  }

  public static void main(String[] args) {
    System.out.println("CANCELLED".equals(AskEventStatus.CANCELLED.name()));
    System.out.println(AskEventStatus.CANCELLED.name());
  }
}
