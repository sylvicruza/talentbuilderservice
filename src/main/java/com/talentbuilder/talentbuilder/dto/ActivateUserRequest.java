package com.talentbuilder.talentbuilder.dto;


public class ActivateUserRequest {
	
	private String activationCode;
	private String password;
	
	public String getActivationCode() {
		return activationCode;
	}
	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	 
	
}
