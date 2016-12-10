package com.ibm.cics.AORprograms.transactions;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.ibm.cics.AORprograms.entities.Account;
import com.ibm.cics.AORprograms.entities.TransHist;
import com.ibm.cics.AORprograms.util.ContainerUtil;
import com.ibm.cics.AORprograms.util.IConstants;
import com.ibm.cics.AORprograms.util.JPAUtil;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.Task;
import com.ibm.cics.server.invocation.CICSProgram;

public class TransactionManagement extends Transaction {

	@CICSProgram("WITHDRAW")
	public void withDraw() {
		System.out.println("Withdraw is being invoked....");
		// Choose the JDBC t4 for db2 conn
		EntityManager em = JPAUtil.getJPAUtilInstance().getEmfType4()
				.createEntityManager();

		String message = null;
		Task task = Task.getTask();
		Channel channel = task.getCurrentChannel();
		if (channel == null) {
			System.out.println("there is no current channel");
		} else {
			String acctNum = ContainerUtil.getContainerData(channel,
					IConstants.TRAN_ACCTNM);
			System.out.println("the accountNum is" + acctNum);
			String amount = ContainerUtil.getContainerData(channel,
					IConstants.TRAN_AMOUNT);
			try {
				InitialContext ctx = new InitialContext();
				UserTransaction tran = (UserTransaction) ctx
						.lookup("java:comp/UserTransaction");

				tran.begin();
				em.joinTransaction();
				Account targetAccount = em.find(Account.class, acctNum);
				System.out.println(targetAccount.getBalance());

				// double d1=Double.valueOf(targetAccount.getBalance());

				double change = Double.parseDouble(amount);
				double now = (Double.parseDouble(targetAccount.getBalance()))
						+ change;

				String balanceNow = String.format("%.2f", now);
				System.out.print(balanceNow);
				targetAccount.setBalance(balanceNow);
				em.merge(targetAccount);

				TransHist transRecord = new TransHist();
				transRecord.setAccountNum(targetAccount.getAccountNumber());
				transRecord.setTransAmount(amount);
				transRecord.setTransName("WITHDRAW");
				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String changeTime = formatter.format(new Date());
				transRecord.setTransTime(changeTime.substring(0, 19));

				em.persist(transRecord);
				em.flush();
				tran.commit();

				System.out.println("Withdraw has been finished");
				message = "Withdraw operation completed, balance for "
						+ acctNum + " is updated. New balance is " + balanceNow;

				ContainerUtil.putContainerData(channel, IConstants.TRAN_CODE,
						"success");

			} catch (NamingException | NotSupportedException | SystemException
					| SecurityException | IllegalStateException
					| RollbackException | HeuristicMixedException
					| HeuristicRollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ContainerUtil.putContainerData(channel, IConstants.TRAN_CODE,
						"failed");
				message = "Withdraw operation for account" + acctNum
						+ " failed";
			}

			// TODO: handle exception
			ContainerUtil.putContainerData(channel, IConstants.TRAN_MSG,
					message);
		}

	}

	@CICSProgram("DEPOSIT")
	public void deposit() {
		System.out.println("Deposit is being invoked....");
		EntityManager em = JPAUtil.getJPAUtilInstance().getEmfType4()
				.createEntityManager();
		String message = null;
		Task task = Task.getTask();
		Channel channel = task.getCurrentChannel();
		if (channel == null) {
			System.out.println("there is no current channel");
		} else {
			String acctNum = ContainerUtil.getContainerData(channel,
					IConstants.TRAN_ACCTNM);
			String amount = ContainerUtil.getContainerData(channel,
					IConstants.TRAN_AMOUNT);

			// check current balance
			Account targetAccount = em.find(Account.class, acctNum);
			String currentBalance = targetAccount.getBalance();

			if (Double.valueOf(currentBalance) - Double.valueOf(amount) < 0) {
				ContainerUtil.putContainerData(channel, IConstants.TRAN_CODE,
						"failed");
				message = "Deposit failed because insufficient balance, current balance for"
						+ acctNum + " is " + currentBalance;
			}
			// update the account balance
			else {
				try {
					InitialContext ctx = new InitialContext();
					UserTransaction tran = (UserTransaction) ctx
							.lookup("java:comp/UserTransaction");
					tran.begin();
					em.joinTransaction();

					double now = Double.valueOf(targetAccount.getBalance())
							- Double.valueOf(amount);
					String balanceNow = String.format("%.2f", now);

					targetAccount.setBalance(balanceNow);
					// update
					em.merge(targetAccount);

					TransHist transRecord = new TransHist();
					transRecord.setAccountNum(targetAccount.getAccountNumber());
					transRecord.setTransAmount(amount);
					transRecord.setTransName("DEPOSIT");
					SimpleDateFormat formatter = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					String changeTime = formatter.format(new Date());
					transRecord.setTransTime(changeTime.substring(0, 19));
					em.persist(transRecord);

					em.flush();
					tran.commit();
					System.out.println("Deposit has been finished");
					ContainerUtil.putContainerData(channel,
							IConstants.TRAN_CODE, "success");
					message = "Deposit operation completed, balance for "
							+ acctNum + " is updated. New balance is "
							+ balanceNow;
				} catch (Exception e) {
					// TODO: handle exception
					ContainerUtil.putContainerData(channel,
							IConstants.TRAN_CODE, "failed");
					message = "Deposit operation for account" + acctNum
							+ " failed";
				}
			}
			ContainerUtil.putContainerData(channel, IConstants.TRAN_MSG,
					message);
			em.close();
		}
	}

}
