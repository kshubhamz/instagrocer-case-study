package com.casestudy.catalogue.listner;

import java.util.concurrent.CountDownLatch;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.casestudy.catalogue.service.CatalogueItemService;
import com.casestudy.instagrocer.commons.RabbitMQMessageListner;
import com.casestudy.instagrocer.commons.constant.InstaGrocerEvent;
import com.casestudy.instagrocer.commons.dto.InventoryProduct;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class InventoryItemCreatedListner implements RabbitMQMessageListner {
	private CountDownLatch latch = new CountDownLatch(1);
	
	@Autowired
	private CatalogueItemService catalogueItemService;
	
	public CountDownLatch getLatch() {
		return latch;
	}

	@Override
	@RabbitListener(queues = { InstaGrocerEvent.INVENTORY_ITEM_CREATED })
	public void onMessageReceive(String message) throws JsonProcessingException {
		InventoryProduct inventoryProduct = new ObjectMapper().readValue(message, InventoryProduct.class);
		catalogueItemService.createCatalogueItem(inventoryProduct);
		latch.countDown();
		
	}

}
