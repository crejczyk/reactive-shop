package com.softmill.shop.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softmill.shop.model.Product;

public class ProductValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Product.class.isAssignableFrom(clazz);
    }

	@Override
	public void validate(Object target, Errors errors) {
		Product product = (Product) target;
		if (product.getId() != null) {
			errors.rejectValue("id", "", "Field 'id' should be empty");
		}
	}
}