package com.jfk.handler;

import com.jfk.base.api.BooleanWrapper;
import com.jfk.base.event.constants.FailureInfo;
import com.jfk.common.distribute.event.domain.AskUserScoreBalance;
import com.jfk.common.event.handler.RevokableAskEventHandler;
import com.jfk.common.spring.ApplicationContextHolder;
import com.jfk.service.ScoreService;

/**
 * Created by wcg on 2016/12/14.
 */
public class AskUserScoreBalanceHandler
    implements RevokableAskEventHandler<AskUserScoreBalance> {

  @Override
  public void processRevoke(AskUserScoreBalance event, FailureInfo failureInfo) {
    ScoreService scoreService = ApplicationContextHolder.context.getBean(ScoreService.class);
    scoreService.revokeBalance(event.getUserId(), event.getSocreA());
  }

  @Override
  public BooleanWrapper processRequest(AskUserScoreBalance event) {
    ScoreService scoreService = ApplicationContextHolder.context.getBean(ScoreService.class);
    return scoreService.processScoreBalance(event.getUserId(), event.getSocreA());
  }

}
