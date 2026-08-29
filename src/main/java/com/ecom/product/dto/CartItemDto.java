package com.ecom.product.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record CartItemDto(
		String productId,
		String variantId,
		BigDecimal price,
		int quantity
	) {
}
