package com.casestudy.user.entity;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.casestudy.user.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Document("users")
public class User {
	@MongoId
	@JsonIgnore
	private ObjectId userId;

	@Field
	@Indexed(unique = true, name = "username_1")
	private String username;

	@Field
	private String name;

	@Field
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	@Field
	@JsonProperty("roles")
	private Set<UserRole> userRoles;

	@Field
	private Instant createdAt;

	public User(String username, String name, String password) {
		this.username = username;
		this.name = name;
		this.password = password;
		this.createdAt = Instant.now();
		initializeRole();
	}

	public User() {
		this.createdAt = Instant.now();
		initializeRole();
	}

	private void initializeRole() {
		this.userRoles = new HashSet<>();
		this.userRoles.add(UserRole.ROLE_USER);
	}

	public ObjectId getUserId() {
		return userId;
	}

	public void setUserId(ObjectId userId) {
		this.userId = userId;
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

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Set<UserRole> getUserRoles() {
		return Collections.unmodifiableSet(userRoles);
	}

	public void addUserRoles(UserRole... userRoles) {
		if (this.userRoles == null) {
			this.userRoles = new HashSet<>();
		}
		this.userRoles.addAll(Arrays.asList(userRoles));
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", name=" + name + "]";
	}

}
