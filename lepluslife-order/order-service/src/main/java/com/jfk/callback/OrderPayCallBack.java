package com.jfk.callback;

import com.jfk.common.distribute.event.domain.AskPartnerScoreBalance;
import com.jfk.common.distribute.event.domain.AskUserScoreBalance;
import com.jfk.common.spring.ApplicationContextHolder;
import com.jfk.service.OrderService;

/**
 * Created by wcg on 2016/12/14.
 */
public class OrderPayCallBack {


  public void onSuccess(AskUserScoreBalance askUserScoreBalance,
                        AskPartnerScoreBalance askPartnerScoreBalance, String orderSid) {
    OrderService orderService = ApplicationContextHolder.context.getBean(OrderService.class);
    orderService.changeOrderStatusToSuccess(orderSid);
  }

  public void onFailure(AskUserScoreBalance askUserScoreBalance,
                        AskPartnerScoreBalance askPartnerScoreBalance, String orderSid) {
    OrderService orderService = ApplicationContextHolder.context.getBean(OrderService.class);
    orderService.changeOrderStatusToFail(orderSid);
  }


}
