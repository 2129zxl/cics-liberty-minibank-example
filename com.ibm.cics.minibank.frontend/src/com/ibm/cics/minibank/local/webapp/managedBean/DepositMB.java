package com.ibm.cics.minibank.local.webapp.managedBean;

import java.io.Serializable;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.Interaction;

import com.ibm.cics.minibank.local.webapp.entity.Account;
import com.ibm.cics.minibank.local.webapp.util.IConstants;
import com.ibm.connector2.cics.ECIChannelRecord;
import com.ibm.connector2.cics.ECIInteractionSpec;

@Named
@RequestScoped
public class DepositMB implements Serializable {
	private static final long serialVersionUID = 1L;
	private @Inject Account targetAccount;
	private Double moneyAmount;
	@Resource(lookup = "eis/ECI")
	private ConnectionFactory cf = null;
	private String operationResult;
	private String operationMessage;

	public DepositMB() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String deposit() {
		try {
			Connection eciConn = cf.getConnection();
			Interaction eciInt = eciConn.createInteraction();
			System.out.println("Create ECI interaction");
			ECIChannelRecord inChannel = new ECIChannelRecord("BANKTRAN");
			inChannel.put(IConstants.TRAN_ACCTNM, targetAccount
					.getAccountNumber().getBytes());
			inChannel.put(IConstants.TRAN_AMOUNT, moneyAmount.toString()
					.getBytes());
			ECIChannelRecord outChannel = new ECIChannelRecord("BANKTRAN");
			// Setup the interactionSpec.
			ECIInteractionSpec eSpec = new ECIInteractionSpec();
			eSpec.setFunctionName("DEPOSIT");
			eSpec.setInteractionVerb(ECIInteractionSpec.SYNC_SEND_RECEIVE);
			System.out.println("Set interaction specification and Execute");

			eciInt.execute(eSpec, inChannel, outChannel);
			System.out.println("Execution completed with response:");

			byte[] rcByte = (byte[]) outChannel.get(IConstants.TRAN_CODE);
			String rcStr = new String(rcByte, "ISO-8859-1");
			byte[] msgByte = (byte[]) outChannel.get(IConstants.TRAN_MSG);
			String msgStr = new String(msgByte, "ISO-8859-1");

			this.setOperationResult(rcStr);
			this.setOperationMessage(msgStr);

			eciInt.close();
			eciConn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this.operationResult.equals("success"))
			return "../notificationpages/notification_deposit_success";
		else
			return "../notificationpages/notification_deposit_failed";
	}

	public Double getMoneyAmount() {
		return moneyAmount;
	}

	public void setMoneyAmount(Double moneyAmount) {
		this.moneyAmount = moneyAmount;
	}

	public String getOperationResult() {
		return operationResult;
	}

	public void setOperationResult(String operationResult) {
		this.operationResult = operationResult;
	}

	public String getOperationMessage() {
		return operationMessage;
	}

	public void setOperationMessage(String operationMessage) {
		this.operationMessage = operationMessage;
	}

	public Account getTargetAccount() {
		return targetAccount;
	}

}
