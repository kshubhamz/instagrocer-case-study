package com.casestudy.instagrocer.commons;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface RabbitMQMessageListner {
	void onMessageReceive(String message) throws JsonProcessingException;
}
