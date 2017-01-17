package com.jfk.service;

import com.jfk.client.OffLineOrderClient;
import com.jfk.client.UserClient;
import com.jfk.distribute.entities.TransactionLock;
import com.jfk.dto.OffLineOrderDto;
import com.jfk.dto.OfflineOrderConsistent;
import com.jfk.dto.UserConsistent;
import com.jfk.dto.UserDto;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by wcg on 2016/12/2.
 */
@Service
public class PayService {

  @Inject
  private OffLineOrderClient offLineOrderClient;

  @Inject
  private UserClient userClient;

  @HystrixCommand(fallbackMethod = "findOffLineOrderByIdFallBack")
  public OffLineOrderDto findOffLineOrderById(Long id) {
    return offLineOrderClient.findOrderById(id);
  }

  public UserDto findUserById(Long id) {
    return userClient.getUser();
  }

  private OffLineOrderDto findOffLineOrderByIdFallBack(Long id) {
    throw new RuntimeException("调用异常");
  }

  public int saveOffLineOrder(OffLineOrderDto offLineOrderDto, TransactionLock transactionLock) {
    offLineOrderClient
        .test2pcDistributeTransaction(new OfflineOrderConsistent(offLineOrderDto, transactionLock));
    return 1;
  }

  public int saveUserByDto(UserDto userDto, TransactionLock transactionLock) {
    userClient.transaction(new UserConsistent(userDto, transactionLock)
    );
    return 1;
  }

  public String testEventDistribute() {
    offLineOrderClient.testEventDistributeTransaction();
    return "OK";
  }

  public String testEventDistributeUnited() {
    offLineOrderClient.testEventDistributeTransactionUnited();
    return "OK";

  }
}
