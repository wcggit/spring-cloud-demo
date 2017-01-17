package com.jfk.controller;

import com.jfk.distribute.entities.TransactionHandler;
import com.jfk.distribute.entities.TransactionLock;
import com.jfk.distribute.transaction.DistributeTransactionManager;
import com.jfk.dto.OffLineOrderDto;
import com.jfk.dto.UserDto;
import com.jfk.service.PayService;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

/**
 * Created by wcg on 2016/12/2.
 */
@RestController
@RequestMapping("/pay")
public class PayController {

  private static Logger logger = Logger.getLogger(PayController.class);

  @Inject
  private PayService payService;

  @RequestMapping("/user/{id}")
  public UserDto findUserById(@PathVariable Long id) {
    return payService.findUserById(id);
  }


  @RequestMapping("/order/{id}")
  public ResponseEntity findOffLineOrderById(@PathVariable Long id) {
    try {
      return new ResponseEntity<OffLineOrderDto>(payService.findOffLineOrderById(id),
                                                 HttpStatus.OK);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("exception");
    }
  }


  @RequestMapping("/order")
  public ResponseEntity testEventDistribute() {
    try {
      return new ResponseEntity<String>(payService.testEventDistribute(),
                                        HttpStatus.OK);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("exception");
    }
  }


  @RequestMapping("/order/share")
  public ResponseEntity testEventDistributeUnited() {
    try {
      return new ResponseEntity<String>(payService.testEventDistributeUnited(),
                                        HttpStatus.OK);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("exception");
    }
  }

  @RequestMapping("/distribute")
  public ResponseEntity distribute() {
    try {
      DistributeTransactionManager transactionManager = DistributeTransactionManager
          .getDistributTransactionManager();

      //第二步：添加分布式服务到管理器中
      transactionManager.pushTransactionHandler(new TransactionHandler() {//分布式服务调用1
        @Override
        public Object execute(TransactionLock transactionLock) {
          UserDto userDto = new UserDto();
          userDto.setUserName("~~~~~~测试用户~~~~~~");
          return payService.saveUserByDto(userDto, transactionLock);
        }
      }).pushTransactionHandler(new TransactionHandler() {//分布式服务调用2
        @Override
        public Object execute(TransactionLock transactionLock) {
          OffLineOrderDto offLineOrderDto = new OffLineOrderDto();
          offLineOrderDto.setSid(UUID.randomUUID().toString());
          return payService.saveOffLineOrder(offLineOrderDto, transactionLock);
        }
      }).startTransaction();//调用startTransaction方法执行
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (RuntimeException e) {
      logger.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body("exception");
    }
  }

}
