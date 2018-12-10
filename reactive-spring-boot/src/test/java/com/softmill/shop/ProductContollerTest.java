package com.softmill.shop;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.softmill.shop.model.Product;
import com.softmill.shop.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@RunWith(SpringRunner.class)
public class ProductContollerTest {

	private static final String SAMPE_PRODUCT_1 = "Product_1";
	private static final String SAMPE_PRODUCT_2 = "Product_2";

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private ProductRepository productRepository;

	@Before
	public void setUp(){
		productRepository.deleteAll().block();
	}

	@Test
	public void createProductTest() {
		Product product = Product.builder().name(SAMPE_PRODUCT_1).price(100.0).build();

		webTestClient.post().uri("/product")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(product), Product.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo(SAMPE_PRODUCT_1);
	}

	@Test
	public void updateProductTest() {
		Product productOrgin = Product.builder().name(SAMPE_PRODUCT_1).price(100.0).build();
		Product ProductDB = productRepository.save(productOrgin).block();
		Product productToUpdate = Product.builder().name(SAMPE_PRODUCT_2).price(200.0).build();

		webTestClient.put().uri(String.format("/product/%s",ProductDB.getId()))
				.contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(productToUpdate), Product.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo(SAMPE_PRODUCT_2);
	}

	@Test
	public void retrieveAllProductsTest() {
		Product product_1 = Product.builder().name(SAMPE_PRODUCT_1).price(100.0).build();
		productRepository.save(product_1).block();

		Product product_2  = Product.builder().name(SAMPE_PRODUCT_2).price(200.0).build();
		productRepository.save(product_2).block();

		webTestClient.get().uri("/products")
                .accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
				.jsonPath("$.[0].name").isEqualTo(SAMPE_PRODUCT_1)
				.jsonPath("$.[1].name").isEqualTo(SAMPE_PRODUCT_2)
            	.consumeWith(consumer -> log.info(new String(consumer.getResponseBody())));
	}


}
