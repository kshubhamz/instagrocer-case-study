package com.casestudy.instagrocer.commons.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface RabbitMQMessage {
	default String toJsonString() throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(this);
	}
}
