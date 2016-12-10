package com.ibm.cics.AORprograms.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CUSTOMER")
public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="CustomerID")
	private String userId;
	@Column(name="Name")
	private String name;
	@Column(name="Gender")
	private char gender;
	@Column(name="Age")
	private String age;
	@Column(name="Address")
	private String address;
	
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public char getGender() {
		return gender;
	}
	public void setGender(char gender) {
		this.gender = gender;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

}
