package com.casestudy.instagrocer.commons.dto;

public class ProductOrdered implements RabbitMQMessage {
	private String productId;
	private Integer quantity;
	private Long catalogueItemId;

	public ProductOrdered(String productId, Integer quantity, Long catalogueItemId) {
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getCatalogueItemId() {
		return catalogueItemId;
	}

	public void setCatalogueItemId(Long catalogueItemId) {
		this.catalogueItemId = catalogueItemId;
	}

}