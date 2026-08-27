package com.ecom.product.document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItem {

	@Field(name = "product_id")
	private String productId;
	
	@Field(name = "variant_id")
	private String variantId;
	
	@Field(name = "price_at_addition")
	private BigDecimal priceAtAddition;
	
	private int quantity;
	
	@Field(name = "added_at")
	private LocalDateTime addedAt;
}
