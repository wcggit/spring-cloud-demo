package com.jfk.common.distribute.event.domain;

import com.jfk.base.event.constants.EventType;
import com.jfk.base.event.domain.AskEvent;
import com.jfk.base.event.domain.Revokable;

/**
 * Created by wcg on 2016/12/14.
 */
public class AskUserScoreBalance extends AskEvent implements Revokable {

  public static final String EVENT_TYPE = EventType.ASK_USER_SCORE_BALANCE.name();

  public Long userId;

  private Long socreA;

  public AskUserScoreBalance() {
  }

  public AskUserScoreBalance(Long userId, Long socreA) {
    this.userId = userId;
    this.socreA = socreA;
  }

  public static String getEventType() {
    return EVENT_TYPE;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Long getSocreA() {
    return socreA;
  }

  public void setSocreA(Long socreA) {
    this.socreA = socreA;
  }

  @Override
  public String getType() {
    return EVENT_TYPE;
  }
}
