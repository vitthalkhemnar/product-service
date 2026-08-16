package com.ecom.product.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.product.document.ProductVariant;
import com.ecom.product.service.ProductVariantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/variant")
@RequiredArgsConstructor
public class ProductVariantController {

	private final ProductVariantService variantService;

	@GetMapping("/{productId}")
	public ResponseEntity<?> getMethodName(@PathVariable("productId") Long productId) {
		List<ProductVariant> variants = variantService.getProductVariants(productId);
		return ResponseEntity.ok().body(variants);
	}

}
