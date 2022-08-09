package com.casestudy.instagrocer.commons.dto;

import java.time.Instant;
import java.util.List;

public class AuthenticatedUser {
	private String name;
	private String username;
	private List<String> roles;
	private Instant createdAt;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public String toString() {
		return "AuthenticatedUser [name=" + name + ", username=" + username + ", roles=" + roles + ", createdAt="
				+ createdAt + "]";
	}

}
