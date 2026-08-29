package com.ecom.product.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.ecom.product.util.ProductStatus;

public record ProductResponse(
    Long id,
    String productCode,
    String productName,
    String description,
    String category,
    String subcategory,
    String brand,
    BigDecimal price,
    BigDecimal discount,
    String material,
    Map<String, Object> attributes,
    List<String> images,
    ProductStatus status
) {}