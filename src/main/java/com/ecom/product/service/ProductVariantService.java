package com.ecom.product.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ecom.product.document.Product;
import com.ecom.product.document.ProductVariant;
import com.ecom.product.dto.VariantResponse;
import com.ecom.product.dto.VariantRequest;
import com.ecom.product.repository.ProductRepository;
import com.ecom.product.repository.ProductVariantRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductVariantService {

	private final ProductVariantRepository variantRepository;
	private final ProductRepository productRepository;
	
	@Cacheable(value = "productService", key="'variant' + #productId")
	public List<VariantResponse> getProductVariantsByProductId(Long productId) {
		return variantRepository.findByProductId(productId).stream().map(this::mapToVariantResponse).toList();	
	}
	
	public boolean deleteVariantById(Long variantId) {
		try {
			ProductVariant variant = variantRepository.findById(variantId)
					.orElseThrow(() -> new RuntimeException("Entity not found."));
			
			Long productId = variant.getProductId();
			variantRepository.deleteById(variantId);
			
			List<ProductVariant> variantList = variantRepository.findByProductId(productId);
			if(variantList == null || variantList.isEmpty()) {
				productRepository.deleteById(productId);
			}
			
			return true;
		} catch (Exception e) {
			log.error("Error while deleting variant with variantId: {}", variantId);
		}
		return false;
	}
	
	public VariantResponse updateVariant(VariantRequest req) {

		ProductVariant variant = variantRepository.findById(req.variantId())
				.orElseThrow(() -> new RuntimeException("Enity not found."));

		if (req.color() != null)
			variant.setColor(req.color());

		if (req.size() != null)
			variant.setSize(req.size());

		if (req.price() != null)
			variant.setPrice(req.price());

		if (req.image() != null)
			variant.setImage(req.image());

		if (req.stock() != null || req.stock() >= 0)
			variant.setStock(req.stock());

		variant.setActive(req.active());

		ProductVariant savedVariant = variantRepository.save(variant);
		return mapToVariantResponse(savedVariant);
	}
	
	public VariantResponse addVariant(VariantRequest req) {
		
		Product product = productRepository.findById(req.productId())
				.orElseThrow(() -> new RuntimeException("Entity not found."));
		
		if(req.variantId() == null)
			throw new RuntimeException("Variant Id should not be null.");

		ProductVariant variant = new ProductVariant();

		variant.setId(req.variantId());
		variant.setProductId(product.getId());
		variant.setActive(req.active());

		if (req.color() != null)
			variant.setColor(req.color());

		if (req.size() != null)
			variant.setSize(req.size());

		if (req.price() != null)
			variant.setPrice(req.price());

		if (req.image() != null)
			variant.setImage(req.image());

		if (req.stock() != null || req.stock() >= 0)
			variant.setStock(req.stock());

		ProductVariant savedVariant = variantRepository.save(variant);
		return mapToVariantResponse(savedVariant);
	}

	public VariantResponse mapToVariantResponse(ProductVariant variant) {
        if (variant == null) {
            return null;
        }

        return new VariantResponse(
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
