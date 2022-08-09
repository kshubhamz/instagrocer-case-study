package com.casestudy.inventory.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Document("products")
@JsonPropertyOrder({ "itemId", "title", "type", "price", "quantity", "description", "createdBy", "createdAt" })
public class Product {

	@Id
	@JsonProperty("itemId")
	private String id;

	@Field
	private String title;

	@JsonProperty("type")
	private String category;

	@Field
	private String description;

	@Field
	private Float price;

	@Field
	private Integer quantity;

	@Field(name = "createdBy")
	@JsonProperty("createdBy")
	private String ownerUsername;
	
	@Field
	private Instant createdAt;

	public Product() {
		this.createdAt = Instant.now();
	}

	public Product(String title, String category, String description, Float price, Integer quantity) {
		this.title = title;
		this.category = category;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.createdAt = Instant.now();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void initialize() {
		this.createdAt = Instant.now();
	}

	public String getOwnerUsername() {
		return ownerUsername;
	}
	
	public void setOwnerUsername(String ownerUsername) {
		this.ownerUsername = ownerUsername;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", title=" + title + ", category=" + category + ", description=" + description
				+ ", price=" + price + "]";
	}

}
