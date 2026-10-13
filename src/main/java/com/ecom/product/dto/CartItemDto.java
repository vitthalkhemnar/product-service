package com.ecom.product.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Representation of an item in the shopping cart")
public record CartItemDto(
		@Schema(description = "ID of the product", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
		String productId,

		@Schema(description = "ID of the product variant", example = "501", requiredMode = Schema.RequiredMode.REQUIRED)
		String variantId,

		@Schema(description = "Price of the item at the time it was added", example = "1499.00")
		BigDecimal price,

		@Schema(description = "Quantity of the product variant", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
		int quantity
	) {
}
