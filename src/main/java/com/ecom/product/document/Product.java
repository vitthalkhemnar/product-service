package com.ecom.product.document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.ecom.product.util.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Document(collection = "products")
public class Product {

	@Id
	private Long id;

	/**
	 * Example: CLO-TSH-ALS-BK-M-100011
	 */
	@Indexed(unique = true)
	@Field("product_code")
	private String productCode;

	@Indexed
	@Field("product_name")
	private String productName;

	private String description;

	/**
	 * Example: CLO
	 */
	@Indexed
	private String category;

	/**
	 * Example: TSH
	 */
	@Indexed
	private String subcategory;

	/**
	 * Example: ALS
	 */
	@Indexed
	private String brand;

	/**
	 * Base/product price.
	 */
	private BigDecimal price;

	/**
	 * Discount percentage.
	 */
	private BigDecimal discount;

	/**
	 * Material code.
	 *
	 * Example: COT
	 */
	private String material;

	/**
	 * Category-specific attributes.
	 *
	 * Examples: ram_gb 
	 * storage_gb 
	 * screen_size_in 
	 * fit_or_style 
	 * isbn 
	 * pages 
	 * volume_ml
	 */
	private Map<String, Object> attributes;

	/**
	 * Product variants.
	 *
	 * A T-Shirt may have: 
	 * Black / M 
	 * Black / L 
	 * White / M 
	 * White / L
	 */
	// private List<ProductVariant> variants;

	/**
	 * Product image URLs.
	 */
	private List<String> images;

	/**
	 * ACTIVE / INACTIVE / OUT_OF_STOCK
	 */
	private ProductStatus status;

	@CreatedDate
	@Field("created_at")
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Field("updated_at")
	private LocalDateTime updatedAt;
}