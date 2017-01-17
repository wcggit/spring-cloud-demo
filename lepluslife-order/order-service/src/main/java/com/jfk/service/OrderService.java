package com.jfk.service;

import com.jfk.callback.OrderPayCallBack;
import com.jfk.common.distribute.event.domain.AskPartnerScoreBalance;
import com.jfk.common.distribute.event.domain.AskUserScoreBalance;
import com.jfk.common.event.AskParameterBuilder;
import com.jfk.common.event.service.EventBus;
import com.jfk.distribute.annotation.DistributeTransaction;
import com.jfk.distribute.entities.TransactionLock;
import com.jfk.domain.OffLineOrder;
import com.jfk.mapper.OffLineOrderMapper;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

/**
 * Created by wcg on 2016/12/2.
 */
@Transactional
@Service
public class OrderService {

  @Inject
  private OffLineOrderMapper offLineOrderMapper;

  @Inject
  private EventBus eventBus;


  public void payOffLineOrderShareToPartner() {
    OffLineOrder order = new OffLineOrder();
    String randomStr = RandomStringUtils.random(5, "1234567890");
    order.setOrderSid(new SimpleDateFormat("yyMMddHHmmss").format(new Date()) + randomStr);
    order.setTotalPrice(100L);
    offLineOrderMapper.insert(order);
    eventBus.ask(
        AskParameterBuilder.askOptional(Optional.of(new AskUserScoreBalance(1L, 100L)),
                                        Optional.of(new AskPartnerScoreBalance(1L, -100L)))
            .callbackClass(OrderPayCallBack.class)
            .addParam("orderSid",
                      String.valueOf(order.getOrderSid()))
            .build()
    );
  }

  public void payOffLineOrder() {
    OffLineOrder order = new OffLineOrder();
    String randomStr = RandomStringUtils.random(5, "1234567890");
    order.setOrderSid(new SimpleDateFormat("yyMMddHHmmss").format(new Date()) + randomStr);
    order.setTotalPrice(100L);
    offLineOrderMapper.insert(order);
    eventBus.ask(
        AskParameterBuilder.askOptional(Optional.of(new AskUserScoreBalance(1L, 100L)))
            .callbackClass(OrderPayCallBack.class)
            .addParam("orderSid",
                      String.valueOf(order.getOrderSid()))
            .build()
    );
  }


  public OffLineOrder findOrderById(Long id) {
    OffLineOrder offLineOrder = offLineOrderMapper.selectByPrimaryKey(id);
    return offLineOrder;
  }

  public void changeOrderStatusToSuccess(String orderSid) {
    OffLineOrder offLineOrder = offLineOrderMapper.selectByOrderSid(orderSid);
    offLineOrder.setOrderStatus(1L);
    offLineOrderMapper.updateByPrimaryKey(offLineOrder);
  }

  public void changeOrderStatusToFail(String orderSid) {
    OffLineOrder offLineOrder = offLineOrderMapper.selectByOrderSid(orderSid);
    offLineOrder.setOrderStatus(2L);
    offLineOrderMapper.updateByPrimaryKey(offLineOrder);
  }

  @DistributeTransaction
  public void test2PcDistribute(OffLineOrder offLineOrder, TransactionLock transactionLock) {
    offLineOrderMapper.insert(offLineOrder);
  }
}
