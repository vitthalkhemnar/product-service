package com.ecom.product.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for adding or updating a product variant")
public record VariantRequest (
    @Schema(description = "Parent product ID", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
	Long productId,

    @Schema(description = "Variant ID (generated if null when adding)", example = "501")
	Long variantId,

    @Schema(description = "Variant color name", example = "Navy Blue")
    String color,

    @Schema(description = "Variant size (e.g., S, M, L, XL, 42)", example = "L")
    String size,

    @Schema(description = "Variant price (falls back to product base price if null)", example = "1499.00")
    BigDecimal price,

    @Schema(description = "Available inventory stock quantity", example = "50")
    Integer stock,

    @Schema(description = "Image URL specific to this variant", example = "https://example.com/images/blue-shirt.jpg")
    String image,

    @Schema(description = "Whether the variant is active and available for sale", example = "true")
    boolean active
) {}