package com.ibm.cics.minibank.local.webapp.util;

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

import java.io.UnsupportedEncodingException;

/**
 * COMMAREA class. Used to create, parse and send/receive data between WOR and
 * JOR, for the TRANSFER transaction
 */
public class TransferCommarea {
	private byte[] SOURCE_ACCT = new byte[10]; // source account to transfer
												// from
	private byte[] TARGET_ACCT = new byte[10]; // target account to transfer to
	private byte[] AMOUNT = new byte[15]; // amount to be transferred
	private byte[] TRANTIME = new byte[19]; // transaction time in
											// "yyyy-MM-dd HH:mm:ss" format
	private byte[] TRANRESULT = new byte[1]; // transaction status. 0 means
												// successful. 1 means failed
	private byte[] MESSAGE = new byte[65]; // detail info of the transaction
											// status
	private byte[] SOURCE_BAL = new byte[15]; // new balance of the source
												// account after transfer
	private byte[] TARGET_BAL = new byte[15]; // new balance of the target
												// account after transfer

	private byte[] COMMAREA = new byte[150]; // the complete COMMAREA buffer

	public TransferCommarea() {
		// TODO Auto-generated constructor stub
		initCommArea();
		initField(SOURCE_ACCT);
		initField(TARGET_ACCT);
		initField(AMOUNT);
		initField(TRANTIME);
		initField(TRANRESULT);
		initField(MESSAGE);
		initField(SOURCE_BAL);
		initField(TARGET_BAL);
	}

	// Create the complete COMMAREA
	public byte[] createCommarea() {
		int offset = 0;
		offset = copyFieldToCommArea(SOURCE_ACCT, offset);
		offset = copyFieldToCommArea(TARGET_ACCT, offset);
		offset = copyFieldToCommArea(AMOUNT, offset);
		offset = copyFieldToCommArea(TRANTIME, offset);
		offset = copyFieldToCommArea(TRANRESULT, offset);
		offset = copyFieldToCommArea(MESSAGE, offset);
		offset = copyFieldToCommArea(SOURCE_BAL, offset);
		offset = copyFieldToCommArea(TARGET_BAL, offset);
		return COMMAREA;
	}

	public void setSourceAccount(String account) {
		byte[] data = account.getBytes();
		int len = Math.min(data.length, SOURCE_ACCT.length);
		System.arraycopy(data, 0, SOURCE_ACCT, 0, len);
	}

	public void setTargetAccount(String account) {
		byte[] data = account.getBytes();
		int len = Math.min(data.length, TARGET_ACCT.length);
		System.arraycopy(data, 0, TARGET_ACCT, 0, len);
	}

	public void setAmount(String amount) {
		int pos = amount.indexOf('.');
		if (pos >= 0) {
			// situation 1: the input has decimal part
			String intPart = amount.substring(0, pos);
			String decimalPart = amount.substring(pos + 1);
			int lenOfIntPart = intPart.length();
			int lenOfDecimal = decimalPart.length();

			byte[] intPartBytes = new byte[12];
			if (lenOfIntPart < intPartBytes.length) {
				for (int i = 0; i < (intPartBytes.length - lenOfIntPart); i++) {
					intPartBytes[i] = '0';
				}
				System.arraycopy(intPart.getBytes(), 0, intPartBytes,
						(intPartBytes.length - lenOfIntPart), lenOfIntPart);
			}

			byte[] decimalBytes = new byte[3];
			for (int i = 0; i < decimalBytes.length; i++) {
				decimalBytes[i] = '0';
			}
			System.arraycopy(decimalPart.getBytes(), 0, decimalBytes, 0,
					Math.min(lenOfDecimal, decimalBytes.length));

			System.arraycopy(intPartBytes, 0, AMOUNT, 0, intPartBytes.length);
			System.arraycopy(decimalBytes, 0, AMOUNT, intPartBytes.length,
					decimalBytes.length);
		} else {
			// situation 2: the input amount does not have decimal part

			byte[] data = amount.getBytes();
			int len = amount.length();

			byte[] intPartBytes = new byte[12];
			if (len < intPartBytes.length) {
				for (int i = 0; i < (intPartBytes.length - len); i++) {
					intPartBytes[i] = '0';
				}
				System.arraycopy(data, 0, intPartBytes,
						(intPartBytes.length - len), len);
			} else {
				System.arraycopy(data, 0, intPartBytes, 0, len);
			}
			System.arraycopy(intPartBytes, 0, AMOUNT, 0, intPartBytes.length);
			AMOUNT[12] = '0';
			AMOUNT[13] = '0';
			AMOUNT[14] = '0';
		}
	}

	public void setTransferTime(String time) {
		byte[] data = time.getBytes();
		int len = Math.min(data.length, TRANTIME.length);
		System.arraycopy(data, 0, TRANTIME, 0, len);
	}

	public String getTranResult() {
		int offset = SOURCE_ACCT.length + TARGET_ACCT.length + AMOUNT.length
				+ TRANTIME.length;
		System.arraycopy(COMMAREA, offset, TRANRESULT, 0, TRANRESULT.length);
		String data = "";

		try {
			data = new String(TRANRESULT, "IBM-1047");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			data = new String(TRANRESULT);
			e.printStackTrace();
		}
		return data;
	}

	public String getTranMessage() {
		int offset = SOURCE_ACCT.length + TARGET_ACCT.length + AMOUNT.length
				+ TRANTIME.length + TRANRESULT.length;
		System.arraycopy(COMMAREA, offset, MESSAGE, 0, MESSAGE.length);

		String data = "";
		try {
			data = new String(MESSAGE, "IBM-1047");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			data = new String(MESSAGE);
			e.printStackTrace();
		}
		return data;
	}

	public String getSourceAcctBalance() {
		int offset = SOURCE_ACCT.length + TARGET_ACCT.length + AMOUNT.length
				+ TRANTIME.length + TRANRESULT.length + MESSAGE.length;
		System.arraycopy(COMMAREA, offset, SOURCE_BAL, 0, SOURCE_BAL.length);

		String data = "";
		String intPart = "";
		String decimal = "";
		try {
			// data = new String(SOURCE_BAL, "IBM-1047");
			String tempStr = new String(SOURCE_BAL);
			byte[] tempData = tempStr.getBytes("file.encoding");
			data = new String(tempData);
			intPart = data.substring(0, 12);
			decimal = data.substring(12);
			data = intPart + "." + decimal;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			data = new String(SOURCE_BAL);
			e.printStackTrace();
		}
		return data;
	}

	public String getTargetAcctBalance() {
		int offset = SOURCE_ACCT.length + TARGET_ACCT.length + AMOUNT.length
				+ TRANTIME.length + TRANRESULT.length + MESSAGE.length
				+ SOURCE_BAL.length;
		System.arraycopy(COMMAREA, offset, TARGET_BAL, 0, TARGET_BAL.length);

		String data = "";
		String intPart = "";
		String decimal = "";
		try {
			// data = new String(TARGET_BAL, "IBM-1047");
			String tempStr = new String(TARGET_BAL);
			byte[] tempData = tempStr.getBytes("file.encoding");
			data = new String(tempData);
			intPart = data.substring(0, 12);
			decimal = data.substring(12);
			data = intPart + "." + decimal;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			data = new String(TARGET_BAL);
			e.printStackTrace();
		}
		return data;
	}

	protected void initCommArea() {
		byte[] tempBuf = new byte[150];
		for (int i = 0; i < tempBuf.length; i++) {
			tempBuf[i] = '0';
		}

		try {
			String tempStr = new String(tempBuf);
			byte[] data = tempStr.getBytes("IBM-1047");
			System.arraycopy(data, 0, COMMAREA, 0, data.length);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.arraycopy(tempBuf, 0, COMMAREA, 0, tempBuf.length);
			e.printStackTrace();
		}

	}

	protected void initField(byte[] field) {
		System.arraycopy(COMMAREA, 0, field, 0, field.length);
	}

	protected int copyFieldToCommArea(byte[] field, int offset) {
		int len = field.length;
		try {
			String tempStr = new String(field);
			byte[] data = tempStr.getBytes("IBM-1047");
			System.arraycopy(data, 0, COMMAREA, offset, data.length);
			len = data.length;
		} catch (UnsupportedEncodingException e) {
			System.arraycopy(field, 0, COMMAREA, offset, field.length);
			len = field.length;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return offset + len;
	}

	/*
	 * Below methods are only used for test purpose
	 */
	public void setTranResult(String result) {
		byte[] data = result.getBytes();
		int len = Math.min(data.length, TRANRESULT.length);
		int offset = SOURCE_ACCT.length + TARGET_ACCT.length + AMOUNT.length
				+ TRANTIME.length;
		System.arraycopy(data, 0, COMMAREA, offset, len);
	}

	public void setTranMessage(String message) {
		byte[] data = message.getBytes();
		int len = Math.min(data.length, MESSAGE.length);
		int offset = SOURCE_ACCT.length + TARGET_ACCT.length + AMOUNT.length
				+ TRANTIME.length + TRANRESULT.length;
		System.arraycopy(data, 0, COMMAREA, offset, len);
	}

	public void setSourceAcctBalance(String balance) {
		byte[] data = balance.getBytes();
		int len = Math.min(data.length, SOURCE_BAL.length);
		int offset = SOURCE_ACCT.length + TARGET_ACCT.length + AMOUNT.length
				+ TRANTIME.length + TRANRESULT.length + MESSAGE.length;
		System.arraycopy(data, 0, COMMAREA, offset, len);
	}

	public void setTargetAcctBalance(String balance) {
		byte[] data = balance.getBytes();
		int len = Math.min(data.length, TARGET_BAL.length);
		int offset = SOURCE_ACCT.length + TARGET_ACCT.length + AMOUNT.length
				+ TRANTIME.length + TRANRESULT.length + MESSAGE.length
				+ SOURCE_BAL.length;
		System.arraycopy(data, 0, COMMAREA, offset, len);
	}

}
