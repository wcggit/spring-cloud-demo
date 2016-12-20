package com.jfk.dto;

import com.jfk.distribute.entities.TransactionLock;

/**
 * Created by wcg on 2016/12/19.
 */
public class UserConsistent {

  private UserDto userDto;

  private TransactionLock transactionLock;

  public UserConsistent(UserDto userDto, TransactionLock transactionLock) {
    this.userDto = userDto;
    this.transactionLock = transactionLock;
  }

  public UserConsistent() {
  }

  public UserDto getUserDto() {
    return userDto;
  }

  public void setUserDto(UserDto userDto) {
    this.userDto = userDto;
  }

  public TransactionLock getTransactionLock() {
    return transactionLock;
  }

  public void setTransactionLock(TransactionLock transactionLock) {
    this.transactionLock = transactionLock;
  }
}
