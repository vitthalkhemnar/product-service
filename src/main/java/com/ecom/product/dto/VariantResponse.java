package com.ecom.product.dto;

import java.math.BigDecimal;

public record VariantResponse(
    Long variantId,
    Long productId,
    String sku,
    String color,
    String size,
    BigDecimal price,
    Integer stock,
    String image,
    boolean active
) {}