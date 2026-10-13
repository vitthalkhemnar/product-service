package com.ecom.product.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.product.dto.CartItemDto;
import com.ecom.product.service.CartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Tag(name = "Shopping Cart", description = "Endpoints for managing user shopping cart items")
public class CartController {

	private final CartService cartService;

	@Operation(
		summary = "Add item to shopping cart",
		description = "Add a product variant with specified quantity to the authenticated user's cart.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@PostMapping("/add")
	public ResponseEntity<List<CartItemDto>> addToCart(@RequestBody CartItemDto cart) {
		return ResponseEntity.ok().body(cartService.addToCart(cart));
	}
	
	@Operation(
		summary = "Remove item from shopping cart",
		description = "Reduce quantity or remove an item from the authenticated user's cart.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@DeleteMapping("/remove")
	public ResponseEntity<List<CartItemDto>> removeFromCart(@RequestBody CartItemDto cart) {
		return ResponseEntity.ok().body(cartService.removeFromCart(cart));
	}

	@Operation(
		summary = "Get user cart items",
		description = "Retrieve all items currently present in the authenticated user's cart.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@GetMapping
	public ResponseEntity<List<CartItemDto>> getCartItems() {
		return ResponseEntity.ok().body(cartService.getCartItems());
	}
}
