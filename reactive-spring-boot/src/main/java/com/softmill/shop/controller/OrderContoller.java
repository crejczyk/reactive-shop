package com.softmill.shop.controller;

import java.util.Date;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softmill.shop.controller.validator.OrderValidator;
import com.softmill.shop.model.Order;
import com.softmill.shop.repository.OrderRepository;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@Validated
public class OrderContoller {

	private OrderRepository orderRepository;

	@InitBinder("order")
	protected void initOrderValidator(WebDataBinder binder) {
		binder.setValidator(new OrderValidator());
	}

	@PostMapping("/order")
	public Mono<Order> createOrder(@Validated(OrderValidator.class) @RequestBody Order order) {
		return orderRepository.save(order);
	}

	@GetMapping("/calOrderPrice/{orderId}")
	public Mono<Double> calculateOrder(@PathVariable(value = "orderId") String orderId) {
		return orderRepository.findById(orderId).flatMap(response -> {
			return Mono.just(response.getProducts().stream().mapToDouble(i -> i.getPrice()).sum());
		});
	}

	@GetMapping("/orders")
	public Flux<Order> getAllByOrderBetween(@RequestParam Long orderTimeStart,
			@RequestParam Long orderTimeEnd) {
		return orderRepository.findAllByOrderTimeBetween(new Date(orderTimeStart), new Date(orderTimeEnd));
	}

}