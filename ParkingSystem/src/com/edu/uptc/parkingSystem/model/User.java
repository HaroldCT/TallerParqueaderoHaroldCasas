package com.edu.uptc.parkingSystem.model;

import java.io.Serializable;

public class User implements Serializable{

	private String UserName;
	private String password;
	
	
	public User() {
		
	}

	public User(String userName, String password) {
		UserName = userName;
		this.password = password;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
}
