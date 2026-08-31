package com.ecom.product.dto;

import java.math.BigDecimal;

public record VariantRequest (
	Long productId,
	Long variantId,
    String color,
    String size,
    BigDecimal price,
    Integer stock,
    String image,
    boolean active
) {}