package com.softmill.shop.controller;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.DoubleStream;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softmill.shop.controller.validator.OrderValidator;
import com.softmill.shop.model.Order;
import com.softmill.shop.model.Product;
import com.softmill.shop.repository.OrderRepository;
import com.softmill.shop.repository.ProductRepository;

import ch.qos.logback.core.util.Duration;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
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

	@GetMapping("/getAllOrders")
	public Flux<Order> getAllOrder() {
		return orderRepository.findAll();
	}

}