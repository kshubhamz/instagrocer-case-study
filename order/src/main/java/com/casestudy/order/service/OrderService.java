package com.casestudy.order.service;

import org.springframework.stereotype.Service;

import com.casestudy.instagrocer.commons.dto.OrderCreated;
import com.casestudy.instagrocer.commons.dto.UnAvailableOrderedProduct;
import com.casestudy.order.dto.ModifyOrderDto;
import com.casestudy.order.dto.OrderDto;
import com.casestudy.order.entity.Order;

@Service
public interface OrderService {
	Order getOrder(Long orderId, String currentUser);
	
	Order placeOrder(OrderDto[] orderDto, String currentUser);

	Order updateOrder(Long orderId, ModifyOrderDto dto, String currentUser);

	void markProductInOrderUnavailable(UnAvailableOrderedProduct unAvailableOrderedProduct);

	void completeOrder(OrderCreated orderCreated);

	void failOrder(OrderCreated orderCreated);
}
