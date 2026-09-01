package com.ecom.product.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.product.dto.VariantRequest;
import com.ecom.product.dto.VariantResponse;
import com.ecom.product.service.ProductVariantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/variant")
@RequiredArgsConstructor
public class ProductVariantController {

	private final ProductVariantService variantService;

	@GetMapping("/{productId}")
	public ResponseEntity<?> getVariantsByProductId(@PathVariable("productId") Long productId) {
		List<VariantResponse> variants = variantService.getProductVariantsByProductId(productId);
		return ResponseEntity.ok().body(variants);
	}
	
	@PostMapping
	public ResponseEntity<?> addVariant(@RequestBody VariantRequest req) {
		return ResponseEntity.ok().body(variantService.addVariant(req));
	}
	
	@PutMapping
	public ResponseEntity<?> updateVariant(@RequestBody VariantRequest req) {
		return ResponseEntity.ok().body(variantService.updateVariant(req));
	}
	
	@DeleteMapping("/{variantId}")
	public ResponseEntity<?> deleteVariant(@PathVariable("variantId") Long variantId) {
		return ResponseEntity.ok().body(variantService.deleteVariantById(variantId));
	}
}
