package com.jfk.distribute.entities;

/**
 *分布式事执行体
 */
public abstract class TransactionHandler{
	
	public abstract Object execute(TransactionLock transactionLock);
	
}
