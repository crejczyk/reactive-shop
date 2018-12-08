package com.softmill.shop.controller.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.softmill.shop.model.Order;

public class OrderValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Order.class.isAssignableFrom(clazz);
    }

	@Override
	public void validate(Object target, Errors errors) {
		Order order = (Order) target;
		if (order.getId() != null) {
			errors.rejectValue("id", "", "Field 'id' should be empty");
		}
	}
}