package com.softmill.shop;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import com.softmill.shop.model.Product;
import com.softmill.shop.repository.OrderRepository;
import com.softmill.shop.repository.ProductRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Configuration
@AllArgsConstructor
@Slf4j
public class LoadDatabase {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

	@EventListener(value = ContextRefreshedEvent.class)
	void init() {
        log.info("Start data initialization  ...");
		initOrdert();
		initProduct();
	}

    private void initOrdert() {
    	orderRepository.deleteAll()
    		.log()
            .subscribe(
                    null,
                    null,
                    () -> log.info("done orders initialization...")
                );
	}

	private void initProduct() {
        this.productRepository
            .deleteAll()
            .thenMany(
                Flux
                    .just(Product.builder().name("Product 1").price(100.00).build(),
                    	  Product.builder().name("Product 2").price(200.00).build()
                    )
                    .flatMap(
                    		product -> this.productRepository.save(product)
                    )
            )
            .log()
            .subscribe(
                null,
                null,
                () -> log.info("done products initialization...")
            );
    }
}