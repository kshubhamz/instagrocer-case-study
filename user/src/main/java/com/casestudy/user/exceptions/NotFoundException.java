package com.casestudy.user.exceptions;

public class NotFoundException extends UserApplicationException {
	private static final long serialVersionUID = -4208600374996182750L;

	public NotFoundException(String message) {
		super(message);
	}
	
}
