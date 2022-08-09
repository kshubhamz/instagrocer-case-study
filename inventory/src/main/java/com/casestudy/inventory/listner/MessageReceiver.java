package com.casestudy.inventory.listner;

import java.util.concurrent.CountDownLatch;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.casestudy.instagrocer.commons.RabbitMQMessageListner;
import com.casestudy.instagrocer.commons.constant.InstaGrocerEvent;
import com.casestudy.instagrocer.commons.dto.OrderCreated;
import com.casestudy.inventory.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MessageReceiver implements RabbitMQMessageListner {
	private CountDownLatch latch = new CountDownLatch(1);

	@Autowired
	private ProductService productService;

	public CountDownLatch getLatch() {
		return latch;
	}

	@Override
	@RabbitListener(queues = { InstaGrocerEvent.ORDER_CREATED })
	public void onMessageReceive(String message) throws JsonProcessingException {
		OrderCreated order = new ObjectMapper().readValue(message, OrderCreated.class);
		productService.reactToOrderCreatedEvent(order);
		latch.countDown();
	}

}
