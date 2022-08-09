package com.casestudy.catalogue.dto;

import com.casestudy.catalogue.entity.CatalogueItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "catalogueItemId", "productId", "title", "category", "price", "description", "quantity", "rating",
		"noOfRatings" })
public class CatalogueItemResponse {
	@JsonProperty("catalogueItemId")
	private Long id;

	private String productId;

	private String title;

	private String category;

	private Float price;

	private String description;

	private Integer quantity;

	private Integer noOfRatings;

	private Double rating;
	
	public static CatalogueItemResponse generateCatalogueItem(CatalogueItem catalogueItem) {
		CatalogueItemResponse catalogueItemResponse = new CatalogueItemResponse();
		
		catalogueItemResponse.setId(catalogueItem.getId());
		catalogueItemResponse.setProductId(catalogueItem.getProductId());
		catalogueItemResponse.setTitle(catalogueItem.getTitle());
		catalogueItemResponse.setCategory(catalogueItem.getCategory());
		catalogueItemResponse.setPrice(catalogueItem.getPrice());
		catalogueItemResponse.setDescription(catalogueItem.getDescription());
		catalogueItemResponse.setQuantity(catalogueItem.getQuantity());
		
		return catalogueItemResponse;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
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

	public Integer getNoOfRatings() {
		return noOfRatings;
	}

	public void setNoOfRatings(Integer noOfRatings) {
		this.noOfRatings = noOfRatings;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

}
