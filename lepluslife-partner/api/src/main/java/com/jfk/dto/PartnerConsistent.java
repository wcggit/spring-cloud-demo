package com.jfk.dto;

import com.jfk.distribute.entities.TransactionLock;

/**
 * Created by wcg on 2016/12/20.
 */
public class PartnerConsistent {

  private PartnerDto partnerDto;

  private TransactionLock transactionLock;

  public PartnerConsistent() {
  }

  public PartnerConsistent(PartnerDto partnerDto,
                           TransactionLock transactionLock) {
    this.partnerDto = partnerDto;
    this.transactionLock = transactionLock;
  }

  public PartnerDto getPartnerDto() {
    return partnerDto;
  }

  public void setPartnerDto(PartnerDto partnerDto) {
    this.partnerDto = partnerDto;
  }

  public TransactionLock getTransactionLock() {
    return transactionLock;
  }

  public void setTransactionLock(TransactionLock transactionLock) {
    this.transactionLock = transactionLock;
  }
}
