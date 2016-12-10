package com.ibm.cics.AORprograms.transactions;

import java.util.List;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

import com.ibm.cics.AORprograms.entities.Account;
import com.ibm.cics.AORprograms.entities.User;
import com.ibm.cics.AORprograms.util.ContainerUtil;
import com.ibm.cics.AORprograms.util.IConstants;
import com.ibm.cics.AORprograms.util.JPAUtil;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.invocation.CICSProgram;

public class UserManagement extends Transaction {

	@CICSProgram("QUERYUSR")
	public void queryUser() {
		System.out.println("Query User is being invoked....");
		EntityManager em = JPAUtil.getJPAUtilInstance().getEmfType4()
				.createEntityManager();

		Task task = Task.getTask();
		Channel channel = task.getCurrentChannel();
		if (channel == null) {
			System.out.println("there is no current channel");
		} else {
			String userId = ContainerUtil.getContainerData(channel,
					IConstants.CUST_ID);
			User resultUser = em.find(User.class, userId);
			if (resultUser == null) {
				ContainerUtil.putContainerData(channel, IConstants.TRAN_CODE,
						"failed");
				em.close();
			} else {
				ContainerUtil.putContainerData(channel, IConstants.CUST_NAME,
						resultUser.getName());
				ContainerUtil.putContainerData(channel, IConstants.CUST_GENDER,
						String.valueOf(resultUser.getGender()));
				ContainerUtil.putContainerData(channel, IConstants.CUST_AGE,
						resultUser.getAge());
				ContainerUtil.putContainerData(channel, IConstants.CUST_ADDR,
						resultUser.getAddress());
				try {
					@SuppressWarnings("rawtypes")
					List queryAccountList = em
							.createQuery(
									"SELECT a FROM Account a WHERE a.customerID = :customerID")
							.setParameter("customerID", userId).getResultList();
					for (int i = 0; i < queryAccountList.size(); i++) {
						Account userAccount = (Account) queryAccountList.get(i);
						// use JSON here
						String accountInfo = userAccount.getAccountNumber()
								+ ";" + userAccount.getBalance() + ";"
								+ userAccount.getLastChangeTime();
						ContainerUtil.putContainerData(channel,
								IConstants.ACCT_LIST + i, accountInfo);
					}
					ContainerUtil.putContainerData(channel,
							IConstants.TRAN_CODE, "success");
				} catch (Exception e) {
					// TODO: handle exception
					ContainerUtil.putContainerData(channel,
							IConstants.TRAN_CODE, "failed");
					System.out.println("query Error");
					e.printStackTrace();
				}
				em.close();
			}
		}

	}

	@CICSProgram("CRETUSER")
	public void createUser() {
		System.out.println("Create User is being invoked....");
		EntityManager em = JPAUtil.getJPAUtilInstance().getEmfType4()
				.createEntityManager();
		String message = null;
		Task task = Task.getTask();
		Channel channel = task.getCurrentChannel();
		if (channel == null) {
			System.out.println("there is no current channel");
		} else {
			String usrId = ContainerUtil.getContainerData(channel,
					IConstants.CUST_ID);
			String usrName = ContainerUtil.getContainerData(channel,
					IConstants.CUST_NAME);
			String usrAge = ContainerUtil.getContainerData(channel,
					IConstants.CUST_AGE);
			String usrGender = ContainerUtil.getContainerData(channel,
					IConstants.CUST_GENDER);
			String usrAddress = ContainerUtil.getContainerData(channel,
					IConstants.CUST_ADDR);

			User newUser = new User();
			newUser.setName(usrName);
			newUser.setAddress(usrAddress);
			newUser.setAge(usrAge);
			newUser.setGender(usrGender.charAt(0));
			newUser.setUserId(usrId);
			// persist the object
			try {
				InitialContext ctx = new InitialContext();
				UserTransaction tran = (UserTransaction) ctx
						.lookup("java:comp/UserTransaction");
				// Start the User Transaction
				tran.begin();
				em.joinTransaction();
				em.persist(newUser);
				tran.commit();
				
				message = "Create User " + usrId + " successful";
				System.out.println(message);
				ContainerUtil.putContainerData(channel, IConstants.TRAN_CODE,
						"success");
				ContainerUtil.putContainerData(channel, IConstants.TRAN_MSG,
						message);
			} catch (Exception e) {
				message = "Create User " + usrId + " failed because this ID already exists";
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
