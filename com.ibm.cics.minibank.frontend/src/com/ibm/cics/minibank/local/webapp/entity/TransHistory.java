package com.ibm.cics.minibank.local.webapp.entity;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

public class TransHistory implements Serializable {

	private static final long serialVersionUID = 1L;
	private String transName;
	private String accountNum;
	private String transAmount;
	private String transTime;

	public TransHistory(String transName, String accountNum,
			String transAmount, String transTime) {
		super();
		this.transName = transName;
		this.accountNum = accountNum;
		this.transAmount = transAmount;
		this.transTime = transTime;
	}

	public TransHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTransName() {
		return transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	public String getAccountNum() {
		return accountNum;
	}

	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}

	public String getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

}
