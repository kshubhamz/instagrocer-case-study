package com.casestudy.user.exceptions;

public class BadRequestException extends UserApplicationException {
	private static final long serialVersionUID = -2299974373646815966L;

	public BadRequestException(String message) {
		super(message);
	}

}
