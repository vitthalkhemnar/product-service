package com.ecom.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.product.request.CartItemDto;
import com.ecom.product.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

	private final CartService cartService;

	@PostMapping("/add")
	public ResponseEntity<?> addToCart(@RequestBody CartItemDto cart) {
		return ResponseEntity.ok().body(cartService.addToCart(cart));
	}

	@GetMapping
	public ResponseEntity<?> getCartItems() {
		return ResponseEntity.ok().body(cartService.getCartItems());
	}
}
