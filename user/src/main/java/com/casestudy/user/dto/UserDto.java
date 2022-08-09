package com.casestudy.user.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.casestudy.user.annotation.UserField;

public class UserDto {
	@NotNull(message = "{username.notnull}")
	@UserField(regex = "^[a-z][a-z0-9\\._-]{4,19}$", message = "{username.message}")
	private String username;

	@NotEmpty(message = "{name.notempty}")
	@Size(max = 100, message = "{name.size}")
	private String name;

	@UserField(regex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,15}$", message = "{password.message}")
	private String password;

	private String role;

	private String oldPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserDto [username=" + username + ", name=" + name + ", role=" + role + "]";
	}

}
