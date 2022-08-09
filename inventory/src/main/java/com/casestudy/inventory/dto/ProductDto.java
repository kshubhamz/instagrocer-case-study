package com.casestudy.inventory.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.casestudy.inventory.annotation.ProductPrice;

public class ProductDto {
	@NotEmpty(message = "{title.notempty}")
	@Size(max = 100, message = "{title.size}")
	private String title;
	
	@NotEmpty(message = "{type.notempty}")
	@Size(max = 20, message = "{type.size}")
	private String type;
	
	@NotEmpty(message = "{description.notempty}")
	@Size(max = 250, message = "{description.size}")
	private String description;
	
	@ProductPrice(min = 0.01f, max = 1000f, message = "{price.productprice}")
	private Float price;
	
	@NotNull(message = "{quantity.notnull}")
	@Min(value = 1, message = "{quantity.min}")
	private Integer quantity;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "ProductDto [title=" + title + ", type=" + type + ", description=" + description + ", price=" + price
				+ ", quantity=" + quantity + "]";
	}

}
