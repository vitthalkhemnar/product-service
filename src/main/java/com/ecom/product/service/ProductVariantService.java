package com.ecom.product.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.ecom.product.document.Product;
import com.ecom.product.document.ProductVariant;
import com.ecom.product.dto.VariantResponse;
import com.ecom.product.dto.VariantRequest;
import com.ecom.product.repository.ProductRepository;
import com.ecom.product.repository.ProductVariantRepository;
import com.ecom.product.util.CommonUtil;
import com.ecom.product.util.SkuConstants;

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
	
	@CacheEvict(value = "productService", allEntries = true)
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
			log.error("Error while deleting variant with variantId: {}", variantId, e);
		}
		return false;
	}
	
	@CacheEvict(value = "productService", key="'variant' + #productId")
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
	
	@CacheEvict(value = "productService", allEntries = true)
	public VariantResponse addVariant(VariantRequest req) {
		
		Product product = productRepository.findById(req.productId())
				.orElseThrow(() -> new RuntimeException("Entity not found."));
		
		Long variantId = (req.variantId() != null) ? req.variantId() : System.currentTimeMillis();

		ProductVariant variant = new ProductVariant();

		variant.setId(variantId);
		variant.setProductId(product.getId());
		variant.setActive(req.active());

		if (req.color() != null)
			variant.setColor(req.color());

		if (req.size() != null)
			variant.setSize(req.size());

		if (req.price() != null)
			variant.setPrice(req.price());
		else
			variant.setPrice(product.getPrice());

		if (req.image() != null)
			variant.setImage(req.image());
		else if (product.getImages() != null && !product.getImages().isEmpty())
			variant.setImage(product.getImages().get(0));

		if (req.stock() != null)
			variant.setStock(req.stock());
		else
			variant.setStock(0);

		String sku = generateVariantSku(product.getProductCode(), req.color(), req.size(), variantId);
		variant.setSku(sku);

		ProductVariant savedVariant = variantRepository.save(variant);
		return mapToVariantResponse(savedVariant);
	}

	private String generateVariantSku(String productCode, String color, String size, Long variantId) {
		String colorCode = (color != null && SkuConstants.COLOR.containsKey(color)) 
				? SkuConstants.COLOR.get(color) : (CommonUtil.isNotBlank(color) ? color.toUpperCase() : SkuConstants.NA);
		String sizeCode = (size != null && SkuConstants.SIZE.containsKey(size)) 
				? SkuConstants.SIZE.get(size) : (CommonUtil.isNotBlank(size) ? size.toUpperCase() : SkuConstants.NA);
		return (productCode != null ? productCode : "PRD") + "-" + colorCode + "-" + sizeCode + "-" + (variantId % 100000);
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
