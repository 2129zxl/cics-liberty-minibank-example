package com.ibm.cics.AORprograms.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class JPAUtil {

	private static JPAUtil instance=null;
	private EntityManagerFactory emfType2;
	private EntityManagerFactory emfType4;
	

	public static JPAUtil getJPAUtilInstance() {
		if ( instance == null ) {
			instance = new JPAUtil();
		}
		return instance;
	}
	
	
	public EntityManagerFactory getEmfType2() {
		if(emfType2==null){
			try {
				emfType2=Persistence.createEntityManagerFactory("type2");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		return emfType2;
	}
	public void setEmfType2(EntityManagerFactory emfType2) {
		this.emfType2 = emfType2;
	}
	
	public EntityManagerFactory getEmfType4() {
		if(emfType4==null){
			emfType4=Persistence.createEntityManagerFactory("type4");
		}
		return emfType4;
	}
	public void setEmfType4(EntityManagerFactory emfType4) {
		this.emfType4 = emfType4;
	}
	
	
	
}
