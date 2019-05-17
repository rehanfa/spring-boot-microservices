package com.code.challange.proxy.common;

import java.util.List;

public class SecurityToken {
	private String userName;
	private String password;
	private List<String> roles;
	private Long loginExpiry;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public Long getLoginExpiry() {
		return loginExpiry;
	}
	public void setLoginExpiry(Long loginExpiry) {
		this.loginExpiry = loginExpiry;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

