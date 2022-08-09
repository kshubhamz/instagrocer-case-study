package com.casestudy.order.listner;

import java.util.concurrent.CountDownLatch;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.casestudy.instagrocer.commons.RabbitMQMessageListner;
import com.casestudy.instagrocer.commons.constant.InstaGrocerEvent;
import com.casestudy.instagrocer.commons.dto.UnAvailableOrderedProduct;
import com.casestudy.order.service.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component("ProductOrderUnAvailableListner")
public class ProductOrderUnAvailableListner implements RabbitMQMessageListner {

	@Autowired
	private OrderService orderService;
	
	private CountDownLatch latch = new CountDownLatch(1);

	public CountDownLatch getLatch() {
		return latch;
	}
	
	@Override
	@RabbitListener(queues = InstaGrocerEvent.PRODUCT_SOME_ORDER_UNAVAILABLE)
	public void onMessageReceive(String message) throws JsonProcessingException {
		UnAvailableOrderedProduct unAvaiableProduct = new ObjectMapper().readValue(message, UnAvailableOrderedProduct.class);
		orderService.markProductInOrderUnavailable(unAvaiableProduct);
		latch.countDown();
	}

}
