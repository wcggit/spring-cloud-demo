package com.jfk.service;

import com.jfk.distribute.annotation.DistributeTransaction;
import com.jfk.distribute.entities.TransactionLock;
import com.jfk.domain.User;
import com.jfk.mapper.UserMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by wcg on 2016/11/14.
 */
@Service
@Transactional
public class UserService {

  @Inject
  private UserMapper userMapper;


  public int userManage() {
    return userMapper.updateByPrimaryKeySelective(userMapper.selectByPrimaryKey(9L));
  }

  public void pessimisticTest() {
    User user = userMapper.exclusiveLock();
    userMapper.updateByPrimaryKeySelective(user);
  }

  @DistributeTransaction
  public void distributeManage(User user, TransactionLock transactionLock) {
    userMapper.insert(user);
  }
}
