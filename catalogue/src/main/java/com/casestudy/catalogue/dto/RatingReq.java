package com.casestudy.catalogue.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RatingReq {
	@NotNull(message = "rating must be defined.")
	@Min(value = 1, message = "rating must be greater than or equal to 1.")
	@Max(value = 5, message = "rating must be less than or equal to 5.")
	private Integer rating;

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

}
