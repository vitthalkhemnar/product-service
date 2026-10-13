package com.ecom.product.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response representation of a product variant")
public record VariantResponse(
    @Schema(description = "Unique variant identifier", example = "501")
    Long variantId,

    @Schema(description = "Associated parent product ID", example = "101")
    Long productId,

    @Schema(description = "Generated SKU identifier for this variant", example = "MEN-CLO-ZAR-101-BLU-L-501")
    String sku,

    @Schema(description = "Color of the variant", example = "Navy Blue")
    String color,

    @Schema(description = "Size of the variant", example = "L")
    String size,

    @Schema(description = "Price of the variant", example = "1499.00")
    BigDecimal price,

    @Schema(description = "Stock quantity available", example = "50")
    Integer stock,

    @Schema(description = "Image URL for this variant", example = "https://example.com/images/blue-shirt.jpg")
    String image,

    @Schema(description = "Whether the variant is currently active", example = "true")
    boolean active
) {}