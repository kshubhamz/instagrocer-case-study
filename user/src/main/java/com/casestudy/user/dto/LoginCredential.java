package com.casestudy.user.dto;

import javax.validation.constraints.NotEmpty;

public class LoginCredential {
	@NotEmpty(message = "{credential.username.notempty}")
	private String username;
	
	@NotEmpty(message = "{credential.password.notempty}")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
