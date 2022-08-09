package com.casestudy.inventory.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.casestudy.inventory.annotation.ProductPrice;

public class ProductPriceValidator implements ConstraintValidator<ProductPrice, Float> {
	
	private Float min;
	private Float max;
	
	@Override
	public void initialize(ProductPrice constraintAnnotation) {
		min = constraintAnnotation.min();
		max = constraintAnnotation.max();
	}
	
	@Override
	public boolean isValid(Float value, ConstraintValidatorContext context) {
		return value != null && value > min && value < max;
	}

}
