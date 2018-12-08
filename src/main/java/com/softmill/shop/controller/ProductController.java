package com.softmill.shop.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.softmill.shop.controller.validator.ProductValidator;
import com.softmill.shop.model.Product;
import com.softmill.shop.repository.ProductRepository;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@Validated
public class ProductController {

	private ProductRepository productRepository;

	@InitBinder("product")
	protected void initProductValidator(WebDataBinder binder) {
		binder.setValidator(new ProductValidator());
	}

	@GetMapping("/products")
	public Flux<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@PostMapping("/product")
	public Mono<Product> createProduct(@Validated(ProductValidator.class) @Valid @RequestBody Product product) {
		return productRepository.save(product);
	}

	@PutMapping("/product/{id}")
	public Mono<ResponseEntity<Product>> updateProduct(@PathVariable(value = "id") String productId,
			@Valid @RequestBody Product product) {
		return productRepository.findById(productId).flatMap(existingProduct -> {
			existingProduct.setName(product.getName());
			existingProduct.setPrice(product.getPrice());
			return productRepository.save(existingProduct);
		}).map(updatedProduct -> new ResponseEntity<>(updatedProduct, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/product/{id}")
	public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable(value = "id") String productId) {
		return productRepository.findById(productId)
				.flatMap(existingProdut -> productRepository.delete(existingProdut)
						.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

}