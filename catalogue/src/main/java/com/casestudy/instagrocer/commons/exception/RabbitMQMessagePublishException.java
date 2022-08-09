package com.casestudy.instagrocer.commons.exception;

public class RabbitMQMessagePublishException extends InstaGrocerApplicationException {
	private static final long serialVersionUID = -1044249695322599591L;

	public RabbitMQMessagePublishException(String message) {
		super(message);
	}

}
