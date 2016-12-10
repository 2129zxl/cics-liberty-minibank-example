package com.ibm.cics.minibank.local.webapp.managedBean;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
import com.ibm.cics.minibank.local.webapp.entity.User;
import com.ibm.cics.minibank.local.webapp.util.IConstants;
import com.ibm.connector2.cics.ECIChannelRecord;
import com.ibm.connector2.cics.ECIInteractionSpec;

@Named
@RequestScoped
public class UserQueryMB implements Serializable {

	private static final long serialVersionUID = 1L;
	private @Inject User queryResult;
	@Resource(lookup = "eis/ECI")
	private ConnectionFactory cf = null;

	public UserQueryMB() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User getQueryResult() {
		return queryResult;
	}

	public String queryUser() throws UnsupportedEncodingException {

		Connection eciConn;
		HashSet<Account> accountsOfUser = new HashSet<Account>();
		try {
			eciConn = cf.getConnection();
			System.out.println("Create ECI connection");
			Interaction eciInt = eciConn.createInteraction();
			System.out.println("Create ECI interaction");

			ECIChannelRecord inChannel = new ECIChannelRecord("BANKTRAN");
			inChannel.put(IConstants.CUST_ID, queryResult.getCustomerID()
					.getBytes());
			ECIChannelRecord outChannel = new ECIChannelRecord("BANKTRAN");

			// Setup the interactionSpec.
			ECIInteractionSpec eSpec = new ECIInteractionSpec();

			eSpec.setFunctionName("QUERYUSR");
			eSpec.setInteractionVerb(ECIInteractionSpec.SYNC_SEND_RECEIVE);
			System.out.println("Set interaction specification and Execute");
			eciInt.execute(eSpec, inChannel, outChannel);

			// get basic userInfo
			byte[] userQueryResultByte = (byte[]) outChannel
					.get(IConstants.TRAN_CODE);
			String userQueryResultString = new String(userQueryResultByte,
					"ISO-8859-1");
			System.out.println(userQueryResultString);

			String userId = new String(
					(byte[]) outChannel.get(IConstants.CUST_ID), "ISO-8859-1");
			String userName = new String(
					(byte[]) outChannel.get(IConstants.CUST_NAME), "ISO-8859-1");
			String userGender = new String(
					(byte[]) outChannel.get(IConstants.CUST_GENDER),
					"ISO-8859-1");
			String userAddress = new String(
					(byte[]) outChannel.get(IConstants.CUST_ADDR), "ISO-8859-1");
			String userAge = new String(
					(byte[]) outChannel.get(IConstants.CUST_AGE), "ISO-8859-1");

			queryResult.setCustomerID(userId);
			queryResult.setUserName(userName);
			if (userGender.equals("f")) {
				queryResult.setUserGender("female");
			} else {
				queryResult.setUserGender("male");
			}

			queryResult.setAge(userAge);
			queryResult.setAddress(userAddress);
			//
			// //get containers
			Set containerSet = outChannel.keySet();
			System.out.println(containerSet.size());
			Iterator containerIterator = containerSet.iterator();
			Object container = null;
			// iterate the containers
			while (containerIterator.hasNext()) {
				container = containerIterator.next();
				if (container != null) {
					if (container.toString().startsWith(IConstants.ACCT_LIST)) {
						String accountRec = container.toString();
						System.out.println(accountRec);
						byte[] accountByte = (byte[]) outChannel
								.get(accountRec);
						String accountString = new String(accountByte,
								"ISO-8859-1");
						System.out.println(accountString);

						Account accountRecord = new Account(
								IConstants.RECORD_NOT_OBTAINED,
								IConstants.RECORD_NOT_OBTAINED,
								IConstants.RECORD_NOT_OBTAINED,
								IConstants.RECORD_NOT_OBTAINED);

						if (accountString
								.contains(IConstants.DATA_FIELD_SPLITTER)) {
							String[] userAccountRecordItem = accountString
									.split(IConstants.DATA_FIELD_SPLITTER);
							String accountNumber = userAccountRecordItem[0];
							String balance = userAccountRecordItem[1];
							String lastChangeTime = userAccountRecordItem[2]
									.substring(0, 19);

							accountRecord.setAccountNumber(accountNumber);
							accountRecord.setBalance(balance);
							accountRecord.setLastChangeTime(lastChangeTime);
						}

						accountsOfUser.add(accountRecord);
					}
				}

			}
			queryResult.setAccountSet(accountsOfUser);
			eciInt.close();
			eciConn.close();
		} catch (ResourceException e) {
			// TODO Auto-generated catch block
			System.out.println("Link exception");
			e.printStackTrace();
		}
		return "queryUserResult";
	}

}
