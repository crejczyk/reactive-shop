package com.softmill.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class ReactiveSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveSpringBootApplication.class, args);
	}
}
