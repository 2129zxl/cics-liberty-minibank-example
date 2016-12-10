package com.ibm.cics.AORprograms.transactions;

import java.util.List;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import com.ibm.cics.AORprograms.entities.Account;
import com.ibm.cics.AORprograms.entities.TransHist;
import com.ibm.cics.AORprograms.util.ContainerUtil;
import com.ibm.cics.AORprograms.util.IConstants;
import com.ibm.cics.AORprograms.util.JPAUtil;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.invocation.CICSProgram;

public class AccountManagement extends Transaction {

	@CICSProgram("QUERYACT")
	public void queryAccount() {
		System.out.println("Query Account is being invoked....");
		// Choose the JDBC Type for db2 conn
		EntityManager em = JPAUtil.getJPAUtilInstance().getEmfType4()
				.createEntityManager();

		Task task = Task.getTask();
		Channel channel = task.getCurrentChannel();
		if (channel == null) {
			System.out.println("there is no current channel");
		} else {
			String acctNum = ContainerUtil.getContainerData(channel,
					IConstants.ACCT_NUMBER);
			Account resultAccount = em.find(Account.class, acctNum);

			if (resultAccount == null) {
				ContainerUtil.putContainerData(channel, IConstants.TRAN_CODE,
						"failed");
				em.close();
			}
			else {
				ContainerUtil.putContainerData(channel, IConstants.TRAN_CODE,
						"success");
				ContainerUtil.putContainerData(channel,
						IConstants.ACCT_CUST_ID, resultAccount.getCustomerID());
				ContainerUtil.putContainerData(channel,
						IConstants.ACCT_BALANCE, resultAccount.getBalance());
				ContainerUtil.putContainerData(channel, IConstants.ACCT_CHANGE,
						resultAccount.getLastChangeTime());
				try {
					@SuppressWarnings("rawtypes")
					List queryTransRecordList = em
							.createQuery(
									"SELECT r FROM TransHist r WHERE r.accountNum = :accountNum")
							.setParameter("accountNum", acctNum)
							.getResultList();

					System.out.println(queryTransRecordList.size());
					for (int i = 0; i < queryTransRecordList.size(); i++) {
						TransHist histRecord = (TransHist) queryTransRecordList
								.get(i);
						String histRecordString = histRecord.getTransName()
								+ ";" + histRecord.getTransTime() + ";"
								+ histRecord.getTransAmount();
						System.out.println(histRecordString);
						ContainerUtil.putContainerData(channel,
								IConstants.HIST_LIST + i, histRecordString);
					}
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("query Error");
					e.printStackTrace();
				}
				em.close();
			}
		}

	}

	@CICSProgram("CREATACT")
	public void createAccount() {
		System.out.println("Create Account is being invoked....");
		EntityManager em = JPAUtil.getJPAUtilInstance().getEmfType4()
				.createEntityManager();
		String message = null;
		Task task = Task.getTask();
		Channel channel = task.getCurrentChannel();
		if (channel == null) {
			System.out.println("there is no current channel");
		} else {
			String acctNum = ContainerUtil.getContainerData(channel,
					IConstants.ACCT_NUMBER);
			String acctCustID = ContainerUtil.getContainerData(channel,
					IConstants.ACCT_CUST_ID);
			String balance = ContainerUtil.getContainerData(channel,
					IConstants.ACCT_BALANCE);
			String changeTime = ContainerUtil.getContainerData(channel,
					IConstants.ACCT_CHANGE);

			Account newAccount = new Account();
			newAccount.setBalance(balance);
			newAccount.setCustomerID(acctCustID);
			newAccount.setLastChangeTime(changeTime);
			newAccount.setAccountNumber(acctNum);
			// persist the object
			try {
				InitialContext ctx = new InitialContext();
				UserTransaction tran = (UserTransaction) ctx
						.lookup("java:comp/UserTransaction");
				// Start the User Transaction
				tran.begin();
				em.joinTransaction();
				em.persist(newAccount);
				tran.commit();

				message =  "Create Account " + acctNum + " successful";
				System.out.println(message);
				ContainerUtil.putContainerData(channel, IConstants.TRAN_CODE,
						"success");
				ContainerUtil.putContainerData(channel, IConstants.TRAN_MSG,
						message);

			} catch (Exception e) {
				// TODO: handle exception
				message = "Create Account " + newAccount.getAccountNumber()
						+ " failed because this ID already exists";
				ContainerUtil.putContainerData(channel, IConstants.TRAN_CODE,
						"failed");
				ContainerUtil.putContainerData(channel, IConstants.TRAN_MSG,
						message);
				e.printStackTrace();
			}
			em.close();
		}
	}
}
