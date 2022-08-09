package com.casestudy.order.listner;

import java.util.concurrent.CountDownLatch;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.casestudy.instagrocer.commons.RabbitMQMessageListner;
import com.casestudy.instagrocer.commons.constant.InstaGrocerEvent;
import com.casestudy.instagrocer.commons.dto.OrderCreated;
import com.casestudy.order.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("OrderFailedListner")
public class OrderFailedListner implements RabbitMQMessageListner {
	@Autowired
	private OrderService orderService;
	
	private CountDownLatch latch = new CountDownLatch(1);

	public CountDownLatch getLatch() {
		return latch;
	}
	
	@Override
	@RabbitListener(queues = InstaGrocerEvent.ORDER_FAILURE)
	public void onMessageReceive(String message) throws JsonProcessingException {
		OrderCreated order = new ObjectMapper().readValue(message, OrderCreated.class);
		orderService.failOrder(order);
		latch.countDown();
	}

}
