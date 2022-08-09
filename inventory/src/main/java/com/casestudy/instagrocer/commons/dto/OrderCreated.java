package com.casestudy.instagrocer.commons.dto;

import java.util.List;

public class OrderCreated implements RabbitMQMessage {
	private Long orderId;
	private List<ProductOrdered> products;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public List<ProductOrdered> getProducts() {
		return products;
	}

	public void setProducts(List<ProductOrdered> products) {
		this.products = products;
	}
	
}
