package com.casestudy.instagrocer.commons.exception;

public abstract class InstaGrocerApplicationException extends RuntimeException {
	private static final long serialVersionUID = 8022351166189401979L;
	private final String message;

	protected InstaGrocerApplicationException(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}
