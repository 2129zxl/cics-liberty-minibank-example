package com.ibm.cics.AORprograms.transactions;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.ibm.cics.AORprograms.entities.Account;
import com.ibm.cics.AORprograms.entities.TransHist;
import com.ibm.cics.AORprograms.util.JPAUtil;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.Task;

public class Transaction {
	
	protected double getAccountBalance(String acctNum) {
		String balance = "0";
		EntityManager em=JPAUtil.getJPAUtilInstance().getEmfType4().createEntityManager();
		Account resultAccount=em.find(Account.class, acctNum);
	    balance=resultAccount.getBalance();
	    double value=Double.valueOf(balance);
	    em.close();
		return value;
	}

	/**
	 * Update an account balance
	 */
	protected int setAccountBalance(String acctNum, double newBalance) {

		EntityManager em=JPAUtil.getJPAUtilInstance().getEmfType2().createEntityManager();
		try {
			em.getTransaction().begin();
			Account resultAccount=em.find(Account.class, acctNum);
			resultAccount.setBalance(String.valueOf(newBalance));
			em.flush();
			em.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 1;
		}
		return 0;
	}

	/**
	 * Add a transaction record into transaction history table
	 */
	protected int addTranHistRecord(String tranName, String acctNum, float amount, String txTime) {
		EntityManager em=JPAUtil.getJPAUtilInstance().getEmfType2().createEntityManager();
		try {
			em.getTransaction().begin();
			TransHist record=new TransHist();
			record.setAccountNum(acctNum);
			record.setTransAmount(String.valueOf(amount));
			record.setTransName(tranName);
			record.setTransTime(txTime);
			em.persist(record);
			em.flush();
			em.getTransaction().commit();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return 1;
		}
		return 0;
	}

}
