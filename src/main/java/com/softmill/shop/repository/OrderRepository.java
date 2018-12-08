package com.softmill.shop.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.softmill.shop.model.Order;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, String> {

}