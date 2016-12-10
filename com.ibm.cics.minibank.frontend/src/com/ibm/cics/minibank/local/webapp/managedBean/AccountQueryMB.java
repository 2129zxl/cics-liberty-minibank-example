package com.ibm.cics.minibank.local.webapp.managedBean;

import java.io.Serializable;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.cci.Interaction;

import com.ibm.cics.minibank.local.webapp.entity.Account;
import com.ibm.cics.minibank.local.webapp.entity.TransHistory;
import com.ibm.cics.minibank.local.webapp.util.IConstants;
import com.ibm.connector2.cics.ECIChannelRecord;
import com.ibm.connector2.cics.ECIInteractionSpec;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

@Named
@RequestScoped
public class AccountQueryMB implements Serializable {

	private static final long serialVersionUID = 1L;
	private @Inject Account queryResult;
	@Resource(lookup = "eis/ECI")
	private ConnectionFactory cf = null;

	public AccountQueryMB() {
		super();
	}

	public Account getQueryResult() {
		return queryResult;
	}

	public String queryAccount() {
		Connection eciConn;
		HashSet<TransHistory> transHistory = new HashSet<TransHistory>();
		try {
			eciConn = cf.getConnection();
			System.out.println("Create ECI connection");
			Interaction eciInt = eciConn.createInteraction();
			System.out.println("Create ECI interaction");
			ECIChannelRecord inChannel = new ECIChannelRecord("BANKTRAN");
			inChannel.put(IConstants.ACCT_NUMBER, queryResult
					.getAccountNumber().getBytes());
			ECIChannelRecord outChannel = new ECIChannelRecord("BANKTRAN");
			// Setup the interactionSpec.
			ECIInteractionSpec eSpec = new ECIInteractionSpec();
			eSpec.setFunctionName("QUERYACT");
			eSpec.setInteractionVerb(ECIInteractionSpec.SYNC_SEND_RECEIVE);
			System.out.println("Set interaction specification and Execute");

			eciInt.execute(eSpec, inChannel, outChannel);

			String result = new String(
					(byte[]) outChannel.get(IConstants.TRAN_CODE), "ISO-8859-1");
			if (result.equals("success")) {
				String lastChangeTime = new String(
						(byte[]) outChannel.get(IConstants.ACCT_CHANGE),
						"ISO-8859-1").substring(0, 19);
				String balance = new String(
						(byte[]) outChannel.get(IConstants.ACCT_BALANCE),
						"ISO-8859-1");
				String customerID = new String(
						(byte[]) outChannel.get(IConstants.ACCT_CUST_ID),
						"ISO-8859-1");

				queryResult.setBalance(balance);
				queryResult.setLastChangeTime(lastChangeTime);
				queryResult.setCustomerID(customerID);
				// //get containers
				Set containerSet = outChannel.keySet();
				System.out.println(containerSet.size());
				Iterator containerIterator = containerSet.iterator();
				Object container = null;
				// get tranHistory
				while (containerIterator.hasNext()) {
					container = containerIterator.next();
					if (container != null) {
						if (container.toString().startsWith(
								IConstants.HIST_LIST)) {
							String histRecord = container.toString();

							System.out.println(histRecord);
							byte[] histByte = (byte[]) outChannel
									.get(histRecord);
							String hisString = new String(histByte,
									"ISO-8859-1");
							System.out.println(hisString);

							TransHistory transHistoryRecord = new TransHistory(
									IConstants.RECORD_NOT_OBTAINED,
									IConstants.RECORD_NOT_OBTAINED,
									IConstants.RECORD_NOT_OBTAINED,
									IConstants.RECORD_NOT_OBTAINED);

							if (hisString
									.contains(IConstants.DATA_FIELD_SPLITTER)) {
								String[] histRecordItems = hisString
										.split(IConstants.DATA_FIELD_SPLITTER);
								String transName = histRecordItems[0];
								String transTime = histRecordItems[1]
										.substring(0, 19);
								String transAmount = histRecordItems[2];

								transHistoryRecord.setTransAmount(transAmount);
								transHistoryRecord.setTransName(transName);
								transHistoryRecord.setTransTime(transTime);
								transHistory.add(transHistoryRecord);
							}
						}
					}

				}
				queryResult.setTransHist(transHistory);
			} else {
				System.out.println("notificationError");
			}
			eciInt.close();

		} catch (ResourceException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "queryAccountResult";
	}

}
