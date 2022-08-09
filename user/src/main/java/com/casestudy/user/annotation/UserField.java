package com.casestudy.user.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.casestudy.user.validator.UserFieldValidator;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserFieldValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface UserField {
	String message() default "Invalid username";

	int min() default 1;

	int max() default Integer.MAX_VALUE;

	String regex() default "[a-zA-Z0-9]";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
