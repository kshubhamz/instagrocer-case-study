package com.casestudy.order.service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import com.casestudy.instagrocer.commons.constant.InstaGrocerRouting;
import com.casestudy.instagrocer.commons.constant.InstaGrocerTopicExchange;
import com.casestudy.instagrocer.commons.dto.OrderCreated;
import com.casestudy.instagrocer.commons.dto.ProductOrdered;
import com.casestudy.instagrocer.commons.dto.UnAvailableOrderedProduct;
import com.casestudy.instagrocer.commons.exception.AuthorizationException;
import com.casestudy.instagrocer.commons.exception.BadRequestException;
import com.casestudy.instagrocer.commons.exception.InvalidDataException;
import com.casestudy.instagrocer.commons.exception.NotFoundException;
import com.casestudy.instagrocer.commons.exception.RabbitMQMessagePublishException;
import com.casestudy.order.dao.ItemRepository;
import com.casestudy.order.dao.OrderRepository;
import com.casestudy.order.dto.ModifyOrderDto;
import com.casestudy.order.dto.OrderDto;
import com.casestudy.order.entity.Item;
import com.casestudy.order.entity.Order;
import com.casestudy.order.enums.OrderItem;
import com.casestudy.order.enums.OrderStatus;
import com.casestudy.order.feignclient.CatalogueServiceProxy;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class OrderServiceImpl implements OrderService {

	@Autowired
	private CatalogueServiceProxy catalogueServiceProxy;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private RabbitMessagingTemplate messagingTemplate;
	
	@Override
	public Order placeOrder(OrderDto[] orders, String currentUser) {
		List<OrderDto> orderDto = Arrays.asList(orders);
		
		// validate dto as list validation is not supported
		boolean isDataInvalid = orderDto.stream().anyMatch(o -> o.getCatalogueItemId() == null
				|| o.getQuantity() == null || (o.getQuantity() != null && o.getQuantity() < 1));
		
		if (isDataInvalid) {
			throw new BadRequestException(
					"Invalid data provided!! Please make sure that itemId and quantity is defined and quantity is greater than 0");
		}
		
		// map orderDto's item to Order's items
		List<Item> items = null;
		Map<Long, String> catProduct = new LinkedHashMap<>();
		try {
			items = orderDto.stream().map(item -> {
				Map<String, Object> fetchByCatalogueId = catalogueServiceProxy
						.fetchByCatalogueId(item.getCatalogueItemId());
				Double price = (Double) fetchByCatalogueId.get("price");
				catProduct.put(item.getCatalogueItemId(), (String)fetchByCatalogueId.get("productId"));
				return new Item(item.getCatalogueItemId(), 
						item.getQuantity(), price, OrderItem.INPROGRESS);
			}).collect(Collectors.toList());
		} catch (ClassCastException | NullPointerException ex) {
			throw new InvalidDataException(ex.getLocalizedMessage());
		}
		
		// calculate total amount
		Double totalAmount = items.stream().mapToDouble(item -> item.getQuantity() * item.getPrice()).sum();
		
		// create order
		Order order = new Order(OrderStatus.INPROGRESS, totalAmount, currentUser);
		
		// add items to order
		items.forEach(order::addItem);
		
		// associate order to items
		items.forEach(item -> item.setOrder(order));
		
		// save order
		Order savedOrder = orderRepository.save(order);
		
		// create order-created object
		OrderCreated orderCreated = new OrderCreated();
		orderCreated.setOrderId(savedOrder.getOrderId());
		orderCreated.setProducts(items.stream()
				.map(item -> new ProductOrdered(catProduct.get(item.getCatalogueItemId()), 
						Long.valueOf(item.getQuantity()), 
						item.getCatalogueItemId()))
				.collect(Collectors.toList()));
		
		// publish order created event
		try {
			messagingTemplate.convertAndSend(
					InstaGrocerTopicExchange.ORDER,
					InstaGrocerRouting.ORDER_CREATED,
					orderCreated.toJsonString()
					);
		} catch (MessagingException | JsonProcessingException e) {
			orderRepository.delete(savedOrder);
			throw new RabbitMQMessagePublishException(e.getLocalizedMessage());
		}
		
		return savedOrder;
	}

	@Override
	public Order updateOrder(Long orderId, ModifyOrderDto dto, String currentUser) {
		
		Optional<Order> orderOpt = orderRepository.findById(orderId);
		if (orderOpt.isEmpty()) {
			throw new NotFoundException(transformOrderNotFoundMessage(orderId));
		}
		Order order = orderOpt.get();
		
		if (!order.getOrderedBy().equals(currentUser)) {
			throw new AuthorizationException("UnAuthorized to cancel this order.");
		}
		
		if (dto.getCancel().booleanValue()) {
			order.setOrderStatus(OrderStatus.CANCELLED);
			order = orderRepository.save(order);
		}
		
		return order;
	}

	@Override
	@Transactional
	public void markProductInOrderUnavailable(UnAvailableOrderedProduct unAvailableOrderedProduct) {
		Long orderId = unAvailableOrderedProduct.getOrderId();
		
		Optional<Order> orderOpt = orderRepository.findById(orderId);
		if (orderOpt.isEmpty()) {
			throw new NotFoundException(transformOrderNotFoundMessage(orderId));
		}
		Order order = orderOpt.get();
		
		Optional<Item> itemOpt = order.getItems()
				.stream()
				.filter(item -> item.getCatalogueItemId()
						.equals(unAvailableOrderedProduct.getCatalogueItemId()))
				.findFirst();
		if (itemOpt.isEmpty()) {
			throw new RuntimeException("No such item " + unAvailableOrderedProduct.getCatalogueItemId() + " exists in order " + orderId + ".");
		}
		
		Item item = itemOpt.get();
		item.setStatus(OrderItem.OUT_OF_STOCK);
		
		// save
		itemRepository.save(item);
		orderRepository.save(order);
	}

	@Override
	@Transactional
	public void completeOrder(OrderCreated orderCreated) {
		Long orderId = orderCreated.getOrderId();
		
		Optional<Order> orderOpt = orderRepository.findById(orderId);
		if (orderOpt.isEmpty()) {
			throw new NotFoundException(transformOrderNotFoundMessage(orderId));
		}
		Order order = orderOpt.get();
		
		// check if any item is out-of-stock
		final List<Item> orderedItems = order.getItems();
		boolean isAnyItemOutOfStock = orderedItems
			.stream()
			.anyMatch(item -> item.getStatus().equals(OrderItem.OUT_OF_STOCK));
		
		OrderStatus orderStatus = isAnyItemOutOfStock ? OrderStatus.PARTIALLY_COMPLETED : OrderStatus.COMPLETED;
		
		order.setOrderStatus(orderStatus);
		
		// update item-status
		orderedItems.forEach(item -> {
			if (!item.getStatus().equals(OrderItem.OUT_OF_STOCK)) {
				item.setStatus(OrderItem.COMPLETED);
				itemRepository.save(item);
			}
		});
		
		// save order
		orderRepository.save(order);
	}

	@Override
	@Transactional
	public void failOrder(OrderCreated orderCreated) {
		Long orderId = orderCreated.getOrderId();
		
		Optional<Order> orderOpt = orderRepository.findById(orderId);
		if (orderOpt.isEmpty()) {
			throw new NotFoundException(transformOrderNotFoundMessage(orderId));
		}
		
		Order order = orderOpt.get();
		
		order.setOrderStatus(OrderStatus.CANCELLED);
		
		// update item-status
		order.getItems().forEach(item -> {
			item.setStatus(OrderItem.CANCELLED);
			itemRepository.save(item);
		});
		
		// save order
		orderRepository.save(order);
	}

	private String transformOrderNotFoundMessage(Long orderId) {
		return "No any order exist with Id: " + orderId;
	}

	@Override
	public Order getOrder(Long orderId, String currentUser) {
		Optional<Order> orderOpt = orderRepository.findById(orderId);
		if (orderOpt.isEmpty()) {
			throw new NotFoundException(transformOrderNotFoundMessage(orderId));
		}
		Order order = orderOpt.get();
		
		if (!order.getOrderedBy().equals(currentUser)) {
			throw new AuthorizationException("UnAuthorized to access details of this order.");
		}
		
		return order;
	}

}
