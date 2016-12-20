package com.jfk.dto;

import com.jfk.distribute.entities.TransactionLock;

/**
 * Created by wcg on 2016/12/19.
 */
public class OfflineOrderConsistent {

  private OffLineOrderDto offLineOrderDto;

  private TransactionLock transactionLock;

  public OfflineOrderConsistent(OffLineOrderDto offLineOrderDto,
                                TransactionLock transactionLock) {
    this.offLineOrderDto = offLineOrderDto;
    this.transactionLock = transactionLock;
  }

  public OfflineOrderConsistent() {
  }

  public OffLineOrderDto getOffLineOrderDto() {
    return offLineOrderDto;
  }

  public void setOffLineOrderDto(OffLineOrderDto offLineOrderDto) {
    this.offLineOrderDto = offLineOrderDto;
  }

  public TransactionLock getTransactionLock() {
    return transactionLock;
  }

  public void setTransactionLock(TransactionLock transactionLock) {
    this.transactionLock = transactionLock;
  }
}
