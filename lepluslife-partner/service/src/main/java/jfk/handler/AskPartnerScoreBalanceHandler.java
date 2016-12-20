package jfk.handler;

import com.jfk.base.api.BooleanWrapper;
import com.jfk.base.event.constants.FailureInfo;
import com.jfk.common.distribute.event.domain.AskPartnerScoreBalance;
import com.jfk.common.event.handler.RevokableAskEventHandler;
import com.jfk.common.spring.ApplicationContextHolder;

import jfk.service.PartnerScoreService;

/**
 * Created by wcg on 2016/12/15.
 */
public class AskPartnerScoreBalanceHandler
    implements RevokableAskEventHandler<AskPartnerScoreBalance> {


  @Override
  public void processRevoke(AskPartnerScoreBalance event, FailureInfo failureInfo) {
    PartnerScoreService
        scoreService =
        ApplicationContextHolder.context.getBean(PartnerScoreService.class);
    scoreService.revokeBalance(event.getPartnerId(), event.getScore());
  }

  @Override
  public BooleanWrapper processRequest(AskPartnerScoreBalance event) {
    PartnerScoreService
        scoreService =
        ApplicationContextHolder.context.getBean(PartnerScoreService.class);
    return scoreService.processScoreBalance(event.getPartnerId(), event.getScore());
  }
}