package com.casestudy.user.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.casestudy.user.annotation.UserField;

public class UserFieldValidator implements ConstraintValidator<UserField, String> {

	private int minLength;
	private int maxLength;
	private String regex;

	@Override
	public void initialize(UserField constraintAnnotation) {
		minLength = constraintAnnotation.min();
		maxLength = constraintAnnotation.max();
		regex = constraintAnnotation.regex();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Pattern pattern = Pattern.compile(regex);
		if (value != null) {
			Matcher matcher = pattern.matcher(value);
			int length = value.length();
			return length >= minLength && length <= maxLength && matcher.matches();
		}
		return true;
	}

}
