package com.ecom.product.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import com.ecom.product.document.Cart;
import com.ecom.product.document.CartItem;
import com.ecom.product.repository.CartRepository;
import com.ecom.product.request.CartItemDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	
	public List<CartItemDto> addToCart(CartItemDto item) {
		
		Cart cart = getCart();
		List<CartItem> items = cart.getItems() == null ? new ArrayList<>() : cart.getItems();

		Optional<CartItem> existingItemOpt = items.stream()
		        .filter(i -> Objects.equals(i.getProductId(), item.productId()) &&
		                     Objects.equals(i.getVariantId(), item.variantId()))
		        .findFirst();

		if (existingItemOpt.isPresent()) {
		    CartItem existingItem = existingItemOpt.get();
		    existingItem.setQuantity(existingItem.getQuantity() + item.quantity());
		    
		    // int index = items.indexOf(existingItem);
		    // items.set(index, existingItem.toBuilder()
		    //         .quantity(existingItem.getQuantity() + item.quantity())
		    //         .build());
		} else {
		    CartItem cartItem = CartItem.builder()
		            .productId(item.productId())
		            .variantId(item.variantId())
		            .priceAtAddition(item.price())
		            .quantity(item.quantity())
		            .addedAt(LocalDateTime.now())
		            .build();
		            
		    items.add(cartItem);
		    cart.setItems(items);
		}
		
		Cart savedCart = cartRepository.save(cart);
		
		List<CartItemDto> cartItems = savedCart.getItems().stream().map(i -> {
			return CartItemDto.builder()
				.productId(i.getProductId())
				.variantId(i.getVariantId())
				.price(i.getPriceAtAddition())
				.quantity(i.getQuantity())
				.build();
		}).toList();
		
		return cartItems;
	}
	
	public Cart getCart() {
		Optional<Cart> cartOpt = cartRepository.findByUsername("vitthal");

		if (cartOpt.isEmpty()) {
			Cart cart = Cart.builder()
				.username("vitthal")
				.build();
			
			return cartRepository.save(cart);
		}
		
		return cartOpt.get();
	}

	public List<CartItemDto> getCartItems() {
		Cart cart = getCart();
		
		if(cart.getItems() == null)
			return List.of();
		
		return cart.getItems().stream().map(i -> {
			return CartItemDto.builder()
				.productId(i.getProductId())
				.variantId(i.getVariantId())
				.price(i.getPriceAtAddition())
				.quantity(i.getQuantity())
				.build();
		}).toList();
	}
}
