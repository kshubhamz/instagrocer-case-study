package com.casestudy.user.exceptions;

public class AuthorizationException extends UserApplicationException {
	private static final long serialVersionUID = 1591092443853050605L;

	public AuthorizationException(String message) {
		super(message);
	}

}
