package com.casestudy.user.dao;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExceptionResponse {
	private String message;
	private List<String> details;
	private final Instant timestamp;

	public ExceptionResponse(String message, List<String> details) {
		this.message = message;
		this.details = details;
		this.timestamp = Instant.now();
	}

	public ExceptionResponse() {
		this.timestamp = Instant.now();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getDetails() {
		return details;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}
	
	public void addDetails(String... details) {
		if (this.details == null) {
			this.details = new ArrayList<>();
		}
		this.details.addAll(Arrays.asList(details));
	}

	public Instant getTimeStamp() {
		return timestamp;
	}

}
