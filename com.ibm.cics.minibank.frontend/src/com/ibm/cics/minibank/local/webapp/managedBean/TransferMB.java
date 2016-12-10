package com.ibm.cics.minibank.local.webapp.managedBean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import com.ibm.cics.minibank.local.webapp.util.JavaStringRecord;
import com.ibm.cics.minibank.local.webapp.util.TransferCommarea;
import com.ibm.connector2.cics.ECIInteractionSpec;

@Named
@RequestScoped
public class TransferMB implements Serializable {

	private static final long serialVersionUID = 1L;
	private @Inject Account targetAccount;
	private @Inject Account sourceAccount;
	private Double moneyAmount;
	@Resource(lookup = "eis/ECI")
	private ConnectionFactory cf = null;
	private String operationResult;
	private String operationMessage;

	public TransferMB() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String transfer() {
		try {
			Connection eciConn = cf.getConnection();
			System.out.println("Create ECI connection");
			Interaction eciInt = eciConn.createInteraction();
			System.out.println("Create ECI interaction");

			JavaStringRecord jsrIn = new JavaStringRecord();
			jsrIn.setEncoding("IBM-1047");

			TransferCommarea transComm = new TransferCommarea();
			transComm.setSourceAccount(sourceAccount.getAccountNumber());
			transComm.setTargetAccount(targetAccount.getAccountNumber());
			transComm.setAmount(String.valueOf(moneyAmount));

			System.out.println(sourceAccount.getAccountNumber() + " to "
					+ targetAccount.getAccountNumber() + ": "
					+ String.valueOf(moneyAmount));

			ByteArrayInputStream inStream = new ByteArrayInputStream(
					transComm.createCommarea());
			jsrIn.read(inStream);
			JavaStringRecord jsrOut = new JavaStringRecord();
			jsrOut.setEncoding("IBM-1047");

			// Setup the interactionSpec.
			ECIInteractionSpec eSpec = new ECIInteractionSpec();
			eSpec.setCommareaLength(150);
			eSpec.setReplyLength(150);
			// "TRANSFER" is the name of the program to execute on CICS
			eSpec.setFunctionName("TRANSFER");
			eSpec.setInteractionVerb(ECIInteractionSpec.SYNC_SEND_RECEIVE);
			System.out.println("Set interaction specification and Execute");

			eciInt.execute(eSpec, jsrIn, jsrOut);
			String[] returnMsgItems = jsrOut.getText().toString().split(" ");

			char resultCode = (returnMsgItems[0].charAt(54));

			// Failed:1 INSUFFIENT AMOUNT
			// Failed:1 THE ACCOUNT IS NOT FOUND
			// Success:0 TRANSFER SUCCESSFULLY
			if (resultCode == '0') {
				this.setOperationResult("success");
				this.setOperationMessage("Account "+sourceAccount.getAccountNumber()
						+" to Account "+targetAccount.getAccountNumber()+" Transfer Successfully with "+moneyAmount.toString());
			} else if (returnMsgItems[1].equals("ACCOUNT")) {
				this.setOperationResult("failed");
				this.setOperationMessage("Operation Transfer Failed. The Account "
						+ targetAccount.getAccountNumber() + " Is Not Found");
			} else if (returnMsgItems[1].equals("AMOUNT")) {
				this.setOperationResult("failed");
				this.setOperationMessage("Insuffient Balance For Account "
						+ sourceAccount.getAccountNumber() + " To Transfer");
			} else {
				this.setOperationResult("failed");
				this.setOperationMessage("Transfer Failed For Some Reason");
			}

			System.out.println("Execution completed with response:"
					+ jsrOut.getText());

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (this.operationResult.equals("success"))
			return "../notificationpages/notification_transfer_success";
		else
			return "../notificationpages/notification_transfer_failed";
	}
	

	public Account getTargetAccount() {
		return targetAccount;
	}

	public Account getSourceAccount() {
		return sourceAccount;
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

}
