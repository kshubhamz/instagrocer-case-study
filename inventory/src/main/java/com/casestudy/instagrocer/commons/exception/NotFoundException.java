package com.casestudy.instagrocer.commons.exception;

public class NotFoundException extends InstaGrocerApplicationException {
	private static final long serialVersionUID = -4208600374996182750L;

	public NotFoundException(String message) {
		super(message);
	}
	
}
