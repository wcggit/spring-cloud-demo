package com.jfk.handler;

import com.jfk.common.distribute.event.domain.UserCreatedEvent;
import com.jfk.common.event.handler.NotifyEventHandler;

/**
 * Created by wcg on 2016/12/8.
 */
public class UserCreatedHandler implements NotifyEventHandler<UserCreatedEvent> {

  public void notify(UserCreatedEvent event) {
    System.out.println(event.toString());
  }
}
