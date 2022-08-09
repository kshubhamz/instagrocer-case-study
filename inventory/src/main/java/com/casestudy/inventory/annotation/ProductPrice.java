package com.casestudy.inventory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.casestudy.inventory.validator.ProductPriceValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Constraint(validatedBy = ProductPriceValidator.class)
public @interface ProductPrice {
	String message() default "Product price is invalid.";
	
	float min() default 0f;
	
	float max() default 1000000f;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
