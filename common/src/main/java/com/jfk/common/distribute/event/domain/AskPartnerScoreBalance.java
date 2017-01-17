package com.jfk.common.distribute.event.domain;

import com.jfk.base.event.constants.EventType;
import com.jfk.base.event.domain.AskEvent;
import com.jfk.base.event.domain.Revokable;

/**
 * Created by wcg on 2016/12/16.
 */
public class AskPartnerScoreBalance extends AskEvent implements Revokable {

  public static final String EVENT_TYPE = EventType.ASK_PARTNER_SCORE_BALANCE.name();

  public Long partnerId;

  private Long score;

  public AskPartnerScoreBalance() {
  }

  public AskPartnerScoreBalance(Long partnerId, Long score) {
    this.partnerId = partnerId;
    this.score = score;
  }

  public static String getEventType() {
    return EVENT_TYPE;
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

  @Override
  public String getType() {
    return EVENT_TYPE;
  }
}
