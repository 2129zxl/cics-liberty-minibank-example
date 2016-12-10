package com.ibm.cics.minibank.local.webapp.util;

import java.io.IOException;

public class JavaStringRecord implements javax.resource.cci.Streamable,
		javax.resource.cci.Record {

	private static final long serialVersionUID = 1L;

	// Internal properties.
	private String recordName;
	private String desc;
	private String contents = new String("");

	/**
	 * The encoding is set to ASCII to avoid platform specific conversion
	 * issues.
	 */
	private String enc = "ASCII"; // default commarea text encoding

	// The following methods are required for the Record interface.

	/**
	 * get the name of the Record
	 * 
	 * @return a String representing the Record Name
	 */
	public java.lang.String getRecordName() {
		return recordName;
	}

	/**
	 * set the name of the Record
	 * 
	 * @param newName
	 *            The Name of the Record
	 */
	public void setRecordName(java.lang.String newName) {
		recordName = newName;
	}

	/**
	 * set the record short description
	 * 
	 * @param newDesc
	 *            The Description for this record
	 */
	public void setRecordShortDescription(java.lang.String newDesc) {
		desc = newDesc;
	}

	/**
	 * get the short description for this Record
	 * 
	 * @return a String representing the Description
	 */
	public java.lang.String getRecordShortDescription() {
		return desc;
	}

	/**
	 * return a hashcode for this object
	 * 
	 * @return hashcode
	 */
	public int hashCode() {
		if (contents != null) {
			return contents.hashCode();
		} else {
			return super.hashCode();
		}
	}

	/**
	 * The following determines if objects are equal. Objects are equal if they
	 * have the same reference or the text contained is identical.
	 * 
	 * @return flag indicating true or false
	 */
	public boolean equals(java.lang.Object comp) {
		if (!(comp instanceof JavaStringRecord)) {
			return false;
		}

		if (comp == this) {
			return true;
		}
		JavaStringRecord realComp = (JavaStringRecord) comp;
		return realComp.getText().equals(this.getText());
	}

	/**
	 * use the superclass clone method for this class
	 */
	public java.lang.Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	// The following methods are required for the streamable interface.

	/**
	 * This method is invoked by the ECI Resource Adapter when it is
	 * transmitting data to the Record. A commarea has been received from the
	 * ECI Resource Adapter and must have been passed as an output record.
	 * 
	 * @param is
	 *            The inputStream containing the information.
	 * @exception IOException
	 *                if an exception occurs on the stream
	 */
	public void read(java.io.InputStream is) {
		try {
			int total = is.available();
			byte[] bytes = null;

			if (total > 0) {
				bytes = new byte[total];
				is.read(bytes);
			}

			// Convert the bytes to a string based on the selected encoding.
			contents = new String(bytes, enc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is invoked by the ECI Resource Adapter when it wants to read
	 * the record. An input record must have been passed in.
	 * 
	 * @param os
	 *            The output stream to write the information to
	 * @exception IOException
	 *                if an exception occurs on the stream
	 */
	public void write(java.io.OutputStream os) {
		try {
			// Output the string as bytes in the selected encoding.
			os.write(contents.getBytes(enc));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Return the text of this Java record.
	 * 
	 * @return The text
	 */
	public String getText() {
		return contents;
	}

	/**
	 * Set the text for this Java record.
	 * 
	 * @param newStr
	 *            The new text
	 */
	public void setText(String newStr) {
		contents = newStr;
	}

	/**
	 * Return the current Java encoding used for this record.
	 * 
	 * @return the Java encoding
	 */
	public String getEncoding() {
		return enc;
	}

	/**
	 * Set the Java encoding to be used for this record.
	 * 
	 * Note: no checks are made at this time to see if the encoding is a valid
	 * Java encoding. If you wish you can modify the code to include this.
	 * 
	 * @param newEnc
	 *            the new Java encoding
	 */
	public void setEncoding(String newEnc) {
		enc = newEnc;
	}
}