package com.softmill.shop.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.softmill.shop.model.Product;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, String> {

}