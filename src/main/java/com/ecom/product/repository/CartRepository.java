package com.ecom.product.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ecom.product.document.Cart;

public interface CartRepository extends MongoRepository<Cart, String> {

	Optional<Cart> findByUsername(String username);
}
