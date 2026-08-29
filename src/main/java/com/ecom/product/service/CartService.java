package com.ecom.product.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ecom.product.document.Cart;
import com.ecom.product.document.CartItem;
import com.ecom.product.repository.CartRepository;
import com.ecom.product.request.CartItemDto;
import com.ecom.product.util.CommonUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
		String username = CommonUtil.getCurrentUsername();
		Optional<Cart> cartOpt = cartRepository.findByUsername(username);

		if (cartOpt.isEmpty()) {
			Cart cart = Cart.builder()
				.username(username)
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

	public List<CartItemDto> removeFromCart(CartItemDto item) {
		
		Cart cart = getCart();
		List<CartItem> items = cart.getItems() == null ? new ArrayList<>() : cart.getItems();

		Optional<CartItem> existingItemOpt = items.stream()
		        .filter(i -> Objects.equals(i.getProductId(), item.productId()) &&
		                     Objects.equals(i.getVariantId(), item.variantId()))
		        .findFirst();

		if (existingItemOpt.isPresent()) {
		    CartItem existingItem = existingItemOpt.get();
		    
		    int newQuantity = existingItem.getQuantity() - item.quantity();
		    
		    if(newQuantity > 0)
			    existingItem.setQuantity(existingItem.getQuantity() - item.quantity());
		    
		    if(newQuantity == 0)
		    	items.remove(existingItem);
		    
		    if(newQuantity < 0)
		    	log.info("Don't have items to remove.");
		    
		    // int index = items.indexOf(existingItem);
		    // items.set(index, existingItem.toBuilder()
		    //         .quantity(existingItem.getQuantity() + item.quantity())
		    //         .build());
		    
		    cart = cartRepository.save(cart);
		}
		
		
		List<CartItemDto> cartItems = cart.getItems().stream().map(i -> {
			return CartItemDto.builder()
				.productId(i.getProductId())
				.variantId(i.getVariantId())
				.price(i.getPriceAtAddition())
				.quantity(i.getQuantity())
				.build();
		}).toList();
		
		return cartItems;
	}
}
