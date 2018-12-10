package com.softmill.shop.model;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "order")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Order {
	@Id
	private String id;

	private List<Product> products;

	@Email(message = "Email should be valid")
	private String contactEmail;

	@Builder.Default
	private Date orderTime = new Date();
}