package com.ibm.cics.minibank.local.webapp.managedBean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.Interaction;

import com.ibm.cics.minibank.local.webapp.entity.Account;
import com.ibm.cics.minibank.local.webapp.util.IConstants;
import com.ibm.connector2.cics.ECIChannelRecord;
import com.ibm.connector2.cics.ECIInteractionSpec;

@Named
@RequestScoped
public class AccountCreateMB implements Serializable {

	private static final long serialVersionUID = 1L;
	private @Inject Account currentAccount;
	@Resource(lookup = "eis/ECI")
	private ConnectionFactory cf = null;
	private String operationResult;
	private String operationMessage;

	public AccountCreateMB() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String createAccount() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String changeTime = formatter.format(new Date());
		try {
			Connection eciConn = cf.getConnection();
			Interaction eciInt = eciConn.createInteraction();
			System.out.println("Create ECI interaction");

			ECIChannelRecord inChannel = new ECIChannelRecord("BANKTRAN");
			inChannel.put(IConstants.ACCT_NUMBER, currentAccount
					.getAccountNumber().getBytes());
			inChannel.put(IConstants.ACCT_CUST_ID, currentAccount
					.getCustomerID().getBytes());
			inChannel.put(IConstants.ACCT_BALANCE, currentAccount.getBalance()
					.getBytes());
			inChannel.put(IConstants.ACCT_CHANGE, changeTime.getBytes());

			ECIChannelRecord outChannel = new ECIChannelRecord("BANKTRAN");
			// Setup the interactionSpec
			ECIInteractionSpec eSpec = new ECIInteractionSpec();
			eSpec.setFunctionName("CREATACT");
			eSpec.setInteractionVerb(ECIInteractionSpec.SYNC_SEND_RECEIVE);
			System.out.println("Set interaction specification and Execute");
			eciInt.execute(eSpec, inChannel, outChannel);
			System.out.println("Execution completed with response:");

			byte[] rcByte = (byte[]) outChannel.get(IConstants.TRAN_CODE);
			String rcStr = new String(rcByte, "ISO-8859-1");
			System.out.println("Return code: " + rcStr);

			byte[] msgByte = (byte[]) outChannel.get(IConstants.TRAN_MSG);
			String msgStr = new String(msgByte, "ISO-8859-1");
			System.out.println("Return message: " + msgStr);

			this.setOperationResult(rcStr);
			this.setOperationMessage(msgStr);
			eciInt.close();
			eciConn.close();

		} catch (ResourceException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.out.println("create Account failed due to some exceptions");
			e.printStackTrace();
		}
		if (this.operationResult.equals("success"))
			return "../notificationpages/notification_createaccount_success";
		else
			return "../notificationpages/notification_createaccount_failed";
	}
	public Account getCurrentAccount() {
		return currentAccount;
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
