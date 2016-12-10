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

import com.ibm.cics.minibank.local.webapp.entity.User;
import com.ibm.cics.minibank.local.webapp.util.IConstants;
import com.ibm.connector2.cics.ECIChannelRecord;
import com.ibm.connector2.cics.ECIInteractionSpec;

@Named
@RequestScoped
public class UserCreateMB implements Serializable {

	private static final long serialVersionUID = 1L;
	private @Inject User currentUser;
	@Resource(lookup = "eis/ECI")
	private ConnectionFactory cf = null;

	private String operationResult;
	private String operationMessage;

	public User getCurrentUser() {
		return currentUser;
	}

	public UserCreateMB() {
		// TODO Auto-generated constructor stub
		super();
	}

	public String createUser() {
		try {
			Connection eciConn = cf.getConnection();
			Interaction eciInt = eciConn.createInteraction();
			System.out.println("Create ECI interaction");

			ECIChannelRecord inChannel = new ECIChannelRecord("BANKTRAN");
			inChannel.put(IConstants.CUST_NAME, currentUser.getUserName()
					.getBytes());
			inChannel.put(IConstants.CUST_ID, currentUser.getCustomerID()
					.getBytes());
			inChannel.put(IConstants.CUST_GENDER, currentUser.getUserGender()
					.getBytes());
			inChannel.put(IConstants.CUST_ADDR, currentUser.getAddress()
					.getBytes());
			inChannel.put(IConstants.CUST_AGE, currentUser.getAge().getBytes());

			ECIChannelRecord outChannel = new ECIChannelRecord("BANKTRAN");
			// Setup the interactionSpec
			ECIInteractionSpec eSpec = new ECIInteractionSpec();
			eSpec.setFunctionName("CRETUSER");
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
		} catch (ResourceException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (this.operationResult.equals("success"))
			return "../notificationpages/notification_createuser_success";
		else
			return "../notificationpages/notification_createuser_failed";
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
