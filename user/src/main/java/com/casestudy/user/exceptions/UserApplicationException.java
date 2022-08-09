package com.casestudy.user.exceptions;

public abstract class UserApplicationException extends RuntimeException {
	private static final long serialVersionUID = 8022351166189401979L;
	private final String message;

	protected UserApplicationException(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
