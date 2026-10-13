package com.ecom.product.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.ecom.product.util.ProductStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for creating or updating a product")
public record ProductRequest(
	@Schema(description = "Unique ID of the product (generated if null on create)", example = "101")
	Long id,

	@Schema(description = "Product SKU code", example = "MEN-CLO-ZAR-101")
    String productCode,

	@Schema(description = "Display name of the product", example = "Men's Slim Fit Cotton Shirt", requiredMode = Schema.RequiredMode.REQUIRED)
    String productName,

	@Schema(description = "Detailed description of the product", example = "100% breathable pure cotton formal shirt.")
    String description,

	@Schema(description = "Top-level category name", example = "Men", requiredMode = Schema.RequiredMode.REQUIRED)
    String category,

	@Schema(description = "Subcategory name", example = "Clothing")
    String subcategory,

	@Schema(description = "Brand manufacturer name", example = "Zara")
    String brand,

	@Schema(description = "Base price of the product", example = "1499.00", requiredMode = Schema.RequiredMode.REQUIRED)
    BigDecimal price,

	@Schema(description = "Discounted price or amount", example = "1199.00")
    BigDecimal discount,

	@Schema(description = "Material composition", example = "Cotton")
    String material,

	@Schema(description = "Custom product attributes key-value pairs")
    Map<String, Object> attributes,

	@Schema(description = "List of image URLs for the product")
    List<String> images,

	@Schema(description = "Catalog status of the product", example = "ACTIVE")
    ProductStatus status
) {
}
