package com.casestudy.instagrocer.commons.dto;

public class ProductOrdered implements RabbitMQMessage {
	private String productId;
	private Long quantity;
	private Long catalogueItemId;

	public ProductOrdered(String productId, Long quantity, Long catalogueItemId) {
		this.productId = productId;
		this.quantity = quantity;
		this.catalogueItemId = catalogueItemId;
	}

	public ProductOrdered() {
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Long getCatalogueItemId() {
		return catalogueItemId;
	}

	public void setCatalogueItemId(Long catalogueItemId) {
		this.catalogueItemId = catalogueItemId;
	}

}