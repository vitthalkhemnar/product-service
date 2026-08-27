package com.ecom.product.document;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Document(collection = "product_variants")
public class ProductVariant {

	@Id
	private Long id;
	
	@Indexed
	@Field("product_id")
	private Long productId;
	
	/**
	 * Example: CLO-TSH-ALS-100011-BK-M (productCode + color + size)
	 */
	@Indexed(unique = true)
	private String sku;

	/**
	 * Color code. Example: BK
	 */
	private String color;

	/**
	 * Size code. Example: M
	 */
	private String size;

	/**
	 * Variant-specific price.
	 */
	private BigDecimal price;

	/**
	 * Available inventory.
	 */
	private Integer stock;

	/**
	 * Variant-specific image.
	 */
	private String image;

	/**
	 * Whether this variant can currently be purchased.
	 */
	private boolean active;
}