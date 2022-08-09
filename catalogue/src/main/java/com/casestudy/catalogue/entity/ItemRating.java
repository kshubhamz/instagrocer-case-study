package com.casestudy.catalogue.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "RATINGS")
public class ItemRating {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RatingIdGenerator")
	@SequenceGenerator(name = "RatingIdGenerator", initialValue = 1, allocationSize = 1)
	@Column(name = "RATING_ID")
	private Long id;

	@Column(name = "END_USER")
	private String username;

	@ManyToOne
	@JoinColumn(name = "ITEM_ID", nullable = false, updatable = false)
	private CatalogueItem catalogueItem;

	@Column(name = "RATING")
	private Integer rating;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	public CatalogueItem getCatalogueItem() {
		return catalogueItem;
	}

	public void setCatalogueItem(CatalogueItem catalogueItem) {
		this.catalogueItem = catalogueItem;
	}

	@Override
	public String toString() {
		return "Raitng [id=" + id + ", username=" + username + ", rating=" + rating + "]";
	}

}
