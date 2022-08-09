package com.casestudy.catalogue.entity;

import java.util.ArrayList;
import java.util.Collections;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CATALOGUE_ITEMS")
public class CatalogueItem {
	@Id
	@Column(name = "ITEM_ID")
	@SequenceGenerator(name = "CatalogueIdGenerator", initialValue = 1001, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CatalogueIdGenerator")
	private Long id;

	@Column(name = "PRODUCT_ID", nullable = false, unique = true, updatable = false)
	private String productId;

	@Column(name = "PRODUCT_TITLE")
	private String title;

	@Column(name = "PRODUCT_CATEGORY")
	private String category;

	@Column(name = "PRODUCT_PRICE")
	private Float price;

	@Column(name = "PRODUCT_DESCRIPTION")
	private String description;

	@Column(name = "AVAILABLE_QUANTITY")
	private Integer quantity;

	@JsonIgnore
	@OneToMany(mappedBy = "catalogueItem", cascade = CascadeType.ALL)
	private List<ItemRating> ratings;

	public CatalogueItem() {
	}

	public CatalogueItem(String productId, String title, String category, Float price, String description,
			Integer quantity) {
		this.productId = productId;
		this.title = title;
		this.category = category;
		this.price = price;
		this.description = description;
		this.quantity = quantity;
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

	public List<ItemRating> getRatings() {
		return Collections.unmodifiableList(ratings);
	}

	public void setRatings(List<ItemRating> ratings) {
		this.ratings = ratings;
	}

	public void addRating(ItemRating rating) {
		if (this.ratings == null) {
			this.ratings = new ArrayList<>();
		}
		this.ratings.add(rating);
	}

	@Override
	public String toString() {
		return "CatalogueItem [id=" + id + ", productId=" + productId + ", title=" + title + ", category=" + category
				+ ", price=" + price + ", description=" + description + ", quantity=" + quantity + "]";
	}

}
