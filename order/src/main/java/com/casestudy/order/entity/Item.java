package com.casestudy.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.casestudy.order.enums.OrderItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ORDERED_ITEMS")
public class Item {

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OrderedItemIdGenerator")
	@SequenceGenerator(name = "OrderedItemIdGenerator", initialValue = 1, allocationSize = 1)
	@Column(name = "ORDEREDITEM_ID")
	private Long id;

	@Column(name = "ITEM_ID")
	private Long catalogueItemId;

	@Column(name = "QUANTITY")
	private Integer quantity;

	@Column(name = "PRICE")
	private Double price;

	@Column(name = "STATUS")
	private OrderItem status;

	@ManyToOne(optional = false)
	@JsonIgnore
	@JoinColumn(name = "ORDER_ID", updatable = false)
	private Order order;

	public Item() {
	}

	public Item(Long catalogueItemId, Integer quantity, Double price, OrderItem status) {
		this.catalogueItemId = catalogueItemId;
		this.quantity = quantity;
		this.price = price;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCatalogueItemId() {
		return catalogueItemId;
	}

	public void setCatalogueItemId(Long catalogueItemId) {
		this.catalogueItemId = catalogueItemId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public OrderItem getStatus() {
		return status;
	}

	public void setStatus(OrderItem status) {
		this.status = status;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
