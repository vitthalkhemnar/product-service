package com.ecom.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecom.product.document.ProductVariant;
import com.ecom.product.dto.ProductVariantResponse;
import com.ecom.product.repository.ProductVariantRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductVariantService {

	private final ProductVariantRepository variantRepository;
	
	public List<ProductVariantResponse> getProductVariants(Long productId) {
		
		return variantRepository.findByProductId(productId).stream().map(this::mapToVariantResponse).toList();	
	}

	public ProductVariantResponse mapToVariantResponse(ProductVariant variant) {
        if (variant == null) {
            return null;
        }

        return new ProductVariantResponse(
            variant.getId(),
            variant.getProductId(),
            variant.getSku(),
            variant.getColor(),
            variant.getSize(),
            variant.getPrice(),
            variant.getStock(),
            variant.getImage(),
            variant.isActive()
        );
    }
}
