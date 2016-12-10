/*
 Copyright IBM Corporation 2014

 LICENSE: Apache License
          Version 2.0, January 2004
          http://www.apache.org/licenses/

 The following code is sample code created by IBM Corporation.
 This sample code is not part of any standard IBM product and
 is provided to you solely for the purpose of assisting you in
 the development of your applications.  The code is provided
 'as is', without warranty or condition of any kind.  IBM shall
 not be liable for any damages arising out of your use of the
 sample code, even if IBM has been advised of the possibility
 of such damages.
 */

/**
 * 
 */
package com.ibm.cics.minibank.local.webapp.util;

/**
 * This interface defines the constants for transactions
 */
public interface IConstants {
	/*
	 * Transaction parameter names, also used for transaction input container
	 * names
	 */
	public final String CUST_ID = "CUST_ID";
	public final String CUST_NAME = "CUST_NAME";
	public final String CUST_GENDER = "CUST_GENDER";
	public final String CUST_AGE = "CUST_AGE";
	public final String CUST_ADDR = "CUST_ADDR";

	public final String ACCT_NUMBER = "ACCT_NUMBER";
	public final String ACCT_CUST_ID = "ACCT_CUST_ID";
	public final String ACCT_BALANCE = "ACCT_BALANCE";
	public final String ACCT_CHANGE = "ACCT_CHANGE";

	public final String TRAN_ACCTNM = "TRAN_ACCTNM";
	public final String TRAN_AMOUNT = "TRAN_AMOUNT";

	public final String HIST_TRANNAME = "HIST_TRANNAME";
	public final String HIST_ACCTNUM = "HIST_ACCTNUM";
	public final String HIST_AMOUNT = "HIST_AMOUNT";
	public final String HIST_TIME = "HIST_TIME";

	/*
	 * Transaction return container names
	 */
	public final String TRAN_CODE = "TRAN_CODE";
	public final String TRAN_MSG = "TRAN_MSG";

	public final String CUST_INFO = "CUST_INFO";
	public final String ACCT_LIST = "ACCT_RECORD_";

	public final String ACCT_INFO = "ACCT_INFO";
	public final String HIST_LIST = "HIST_RECORD_";

	/*
	 * splitter for multiple data fields. Used to split fields from a record
	 * string from database
	 */
	public final String DATA_FIELD_SPLITTER = ";";

	public final String TRAN_DEPOSIT = "DEPOSIT";
	public final String TRAN_WITHDRAW = "WITHDRAW";
	public final String TRAN_TRANSFER = "TRANSFER";

	/*
	 * exception processing
	 */
	public final String RECORD_NOT_OBTAINED = "not obtained";
}
