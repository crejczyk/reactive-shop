package com.softmill.shop;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.softmill.shop.model.Order;
import com.softmill.shop.model.Product;
import com.softmill.shop.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@RunWith(SpringRunner.class)
public class OrderContollerTest {

	private static final String SAMPE_PRODUCT_1 = "Product_1";
	private static final String SAMPE_PRODUCT_2 = "Product_2";
	private static final String CONTACT_EMAIL = "test@gmail.com";

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private OrderRepository orderRepository;

	@Before
	public void setUp(){
		orderRepository.deleteAll().block();
	}

	@Test
	public void createOrderTest() {
		Product product1= Product.builder().name(SAMPE_PRODUCT_1).build();
		Product product2 = Product.builder().name(SAMPE_PRODUCT_2).build();
		List<Product> products = new ArrayList<Product>( Arrays.asList(product1,product2));

		Order order = Order.builder().contactEmail(CONTACT_EMAIL).products(products).build();
		webTestClient.post().uri("/order")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(order), Order.class)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.contactEmail").isEqualTo(CONTACT_EMAIL)
				.jsonPath("$.products[0].name").isEqualTo(SAMPE_PRODUCT_1)
				.jsonPath("$.products[1].name").isEqualTo(SAMPE_PRODUCT_2);
	}

	@Test
	public void calOrderPriceTest() {
		Double price1 = 100.0;
		Double price2 = 123.0;
		String orderId = initOrder(price1, price2);

		webTestClient.get().uri(String.format("/calOrderPrice/%s",orderId))
	        .accept(MediaType.APPLICATION_JSON_UTF8)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody()
			.jsonPath("$").isEqualTo(price1 + price2);
	}

	@Test
	public void getAllByOrderBetweenTest() {
		String order1 = initOrder(100.0, 200.0);
		String order2 = initOrder(100.0, 200.0);

		Date orderDate1 = java.sql.Date.valueOf(LocalDate.parse("2015-02-20"));
		Date orderDate2 = java.sql.Date.valueOf(LocalDate.parse("2018-02-20"));

		Order orderDB1 = orderRepository.findById(order1).block();
		orderDB1.setOrderTime(orderDate1);
		orderRepository.save(orderDB1).block();

		Order orderDB2 = orderRepository.findById(order2).block();
		orderDB2.setOrderTime(orderDate2);
		orderRepository.save(orderDB2).block();

		Date searchStart = java.sql.Date.valueOf(LocalDate.parse("2015-02-21"));
		Date searchEnd = java.sql.Date.valueOf(LocalDate.parse("2018-02-21"));

		webTestClient.get().uri(String.format("/orders?orderTimeStart=%s&orderTimeEnd=%s", searchStart.getTime(), searchEnd.getTime()))
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody()
			.jsonPath("$[0].id").isEqualTo(orderDB2.getId())
			.consumeWith(consumer -> log.info(new String(consumer.getResponseBody())));
	}


	private String initOrder(Double price1, Double price2) {
		Product product1= Product.builder().name(SAMPE_PRODUCT_1).price(price1).build();
		Product product2 = Product.builder().name(SAMPE_PRODUCT_2).price(price2).build();
		List<Product> products = new ArrayList<Product>( Arrays.asList(product1,product2));
		Order order = Order.builder().contactEmail(CONTACT_EMAIL).products(products).build();
		return orderRepository.save(order).block().getId();
	}

}
