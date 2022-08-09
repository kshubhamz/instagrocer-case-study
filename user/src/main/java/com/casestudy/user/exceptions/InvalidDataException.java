package com.casestudy.user.exceptions;

public class InvalidDataException extends UserApplicationException {
	private static final long serialVersionUID = -2216742126431837976L;

	public InvalidDataException(String message) {
		super(message);
	}

}
