package com.ecom.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecom.product.document.ProductVariant;
import com.ecom.product.repository.ProductVariantRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductVariantService {

	private final ProductVariantRepository variantRepository;
	
	public List<ProductVariant> getProductVariants(Long productId) {
		
		return variantRepository.findByProductId(productId);	
	}
	
}
