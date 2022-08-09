package com.casestudy.order.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.casestudy.order.enums.OrderStatus;

@Entity
@Table(name = "ORDERS")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OrderIdGenerator")
	@SequenceGenerator(name = "OrderIdGenerator", initialValue = 2001, allocationSize = 1)
	@Column(name = "ORDER_ID")
	private Long orderId;

	@Column(name = "ORDER_STATUS")
	private OrderStatus orderStatus;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<Item> items = new ArrayList<>();

	@Column(name = "TOTAL_AMOUNT")
	private Double totalAmount;

	@Column(name = "ORDERED_BY")
	private String orderedBy;

	public Order() {
	}

	public Order(OrderStatus orderStatus, Double totalAmount, String orderedBy) {
		this.orderStatus = orderStatus;
		this.totalAmount = totalAmount;
		this.orderedBy = orderedBy;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public List<Item> getItems() {
		return items;
	}

	public void addAllItem(Item... item) {
		if (this.items == null) {
			this.items = new ArrayList<>();
		}
		this.items.addAll(Arrays.asList(item));
	}
	
	public void addItem(Item item) {
		if (this.items == null) {
			this.items = new ArrayList<>();
		}
		this.items.add(item);
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getOrderedBy() {
		return orderedBy;
	}

	public void setOrderedBy(String orderedBy) {
		this.orderedBy = orderedBy;
	}

}
