package com.casestudy.order.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderDto {

	@JsonProperty("itemId")
	@NotNull(message = "ItemId must be defined.")
	private Long catalogueItemId;

	@NotNull(message = "Quantity must be defined.")
	@Min(value = 1, message = "Quantity must be greater than or equal to 1.")
	private Integer quantity;

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
}
