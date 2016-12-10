package com.ibm.cics.AORprograms.entities;


import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name="TRANSHISTORY")
public class TransHist implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	@Column(name="TransName")
	private String transName;
	@Column(name="AccountNumber")
	private String accountNum;
	@Column(name="TransAmount")
	private String transAmount;
	@Id
	@Column(name="TransTime")
	private String transTime;
	
	public TransHist() {
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
