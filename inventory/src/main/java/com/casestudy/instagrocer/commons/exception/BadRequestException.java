package com.casestudy.instagrocer.commons.exception;

public class BadRequestException extends InstaGrocerApplicationException {
	private static final long serialVersionUID = -2299974373646815966L;

	public BadRequestException(String message) {
		super(message);
	}

}
