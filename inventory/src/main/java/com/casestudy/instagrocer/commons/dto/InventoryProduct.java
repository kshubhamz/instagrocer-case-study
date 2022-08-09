package com.casestudy.instagrocer.commons.dto;

import com.casestudy.inventory.entity.Product;

public class InventoryProduct implements RabbitMQMessage {
	private String productId;
	private String title;
	private String category;
	private String description;
	private Integer quantity;
	private Float price;
	
	public static InventoryProduct createMessagableInventoryProduct(Product product) {
		InventoryProduct inventoryProduct = new InventoryProduct();
		
		inventoryProduct.setProductId(product.getId());
		inventoryProduct.setTitle(product.getTitle());
		inventoryProduct.setCategory(product.getCategory());
		inventoryProduct.setDescription(product.getDescription());
		inventoryProduct.setQuantity(product.getQuantity());
		inventoryProduct.setPrice(product.getPrice());
		
		return inventoryProduct;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

}
