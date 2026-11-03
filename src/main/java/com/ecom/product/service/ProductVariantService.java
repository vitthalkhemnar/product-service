package com.ecom.product.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.product.document.Product;
import com.ecom.product.document.ProductVariant;
import com.ecom.product.dto.StockUpdateRequest;
import com.ecom.product.dto.VariantRequest;
import com.ecom.product.dto.VariantResponse;
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
	private final MongoTemplate mongoTemplate;
	
	@Cacheable(value = "productService", key="'variant' + #productId")
	public List<VariantResponse> getProductVariantsByProductId(Long productId) {
		return variantRepository.findByProductId(productId).stream().map(this::mapToVariantResponse).toList();	
	}

	public VariantResponse getVariantById(Long variantId) {
		if (variantId == null) {
			return null;
		}
		return variantRepository.findById(variantId)
				.map(this::mapToVariantResponse)
				.orElse(null);
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
	
	@CacheEvict(value = "productService", allEntries = true)
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

		if (req.stock() != null && req.stock() >= 0)
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

	@Transactional
	@CacheEvict(value = "productService", allEntries = true)
	public boolean deductStock(List<StockUpdateRequest> items) {
		if (items == null || items.isEmpty()) {
			return true;
		}

		// 1. First pass: verify availability of each requested item
		for (StockUpdateRequest item : items) {
			if (item.variantId() == null) {
				throw new IllegalArgumentException("Variant ID is required for stock deduction.");
			}
			if (item.quantity() == null || item.quantity() <= 0) {
				throw new IllegalArgumentException("Quantity must be greater than zero for variant ID: " + item.variantId());
			}

			ProductVariant variant = variantRepository.findById(item.variantId())
					.orElseThrow(() -> new IllegalArgumentException("Variant not found with ID: " + item.variantId()));

			if (!variant.isActive()) {
				throw new IllegalArgumentException("Variant " + (variant.getSku() != null ? variant.getSku() : item.variantId()) + " is not currently active.");
			}

			int currentStock = (variant.getStock() != null) ? variant.getStock() : 0;
			if (currentStock < item.quantity()) {
				throw new IllegalArgumentException(String.format(
						"Insufficient stock for variant %s. Requested: %d, Available: %d",
						variant.getSku() != null ? variant.getSku() : String.valueOf(item.variantId()),
						item.quantity(),
						currentStock
				));
			}
		}

		// 2. Second pass: perform atomic decrements
		List<StockUpdateRequest> successfulDeductions = new ArrayList<>();
		for (StockUpdateRequest item : items) {
			Query query = new Query(Criteria.where("_id").is(item.variantId()).and("stock").gte(item.quantity()));
			Update update = new Update().inc("stock", -item.quantity());
			ProductVariant updated = mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), ProductVariant.class);

			if (updated == null) {
				// Concurrent purchase occurred between first-pass check and decrement:
				// Compensate any items deducted in this batch before failing
				if (!successfulDeductions.isEmpty()) {
					restoreStock(successfulDeductions);
				}
				throw new IllegalStateException("Stock deduction failed due to concurrent order for variant ID: " + item.variantId() + ". Please try again.");
			}
			successfulDeductions.add(item);
		}

		log.info("Successfully deducted inventory stock for {} variant(s)", items.size());
		return true;
	}

	@Transactional
	@CacheEvict(value = "productService", allEntries = true)
	public boolean restoreStock(List<StockUpdateRequest> items) {
		if (items == null || items.isEmpty()) {
			return true;
		}

		for (StockUpdateRequest item : items) {
			if (item.variantId() == null || item.quantity() == null || item.quantity() <= 0) {
				continue;
			}
			Query query = new Query(Criteria.where("_id").is(item.variantId()));
			Update update = new Update().inc("stock", item.quantity());
			mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), ProductVariant.class);
		}

		log.info("Successfully restored inventory stock for {} variant(s)", items.size());
		return true;
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
