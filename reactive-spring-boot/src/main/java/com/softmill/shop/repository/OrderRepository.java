package com.softmill.shop.repository;

import java.util.Date;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.softmill.shop.model.Order;

import reactor.core.publisher.Flux;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, String> {

	Flux<Order> findAllByOrderTimeBetween(Date orderTimeStart, Date orderTimeEnd);

}