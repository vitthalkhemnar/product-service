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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/variant")
@RequiredArgsConstructor
@Tag(name = "Product Variants", description = "Endpoints for managing product variants including color, size, stock, and SKU")
public class ProductVariantController {

	private final ProductVariantService variantService;

	@Operation(
		summary = "Get variants by product ID",
		description = "Retrieve all active and configured variants for a specific product. Public endpoint."
	)
	@GetMapping("/{productId}")
	public ResponseEntity<List<VariantResponse>> getVariantsByProductId(
			@Parameter(description = "Product ID to fetch variants for", required = true, example = "101")
			@PathVariable("productId") Long productId) {
		List<VariantResponse> variants = variantService.getProductVariantsByProductId(productId);
		return ResponseEntity.ok().body(variants);
	}
	
	@Operation(
		summary = "Add a new variant",
		description = "Create a new variant for an existing product. Requires ADMIN role.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@PostMapping
	public ResponseEntity<VariantResponse> addVariant(
			@RequestBody VariantRequest req) {
		return ResponseEntity.ok().body(variantService.addVariant(req));
	}
	
	@Operation(
		summary = "Update a variant",
		description = "Update variant details such as price, stock, size, or color. Requires ADMIN role.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@PutMapping
	public ResponseEntity<VariantResponse> updateVariant(
			@RequestBody VariantRequest req) {
		return ResponseEntity.ok().body(variantService.updateVariant(req));
	}
	
	@Operation(
		summary = "Delete a variant",
		description = "Delete a variant by its ID. If it is the last variant, the product may be cleaned up. Requires ADMIN role.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@DeleteMapping("/{variantId}")
	public ResponseEntity<Boolean> deleteVariant(
			@Parameter(description = "ID of the variant to delete", required = true, example = "501")
			@PathVariable("variantId") Long variantId) {
		return ResponseEntity.ok().body(variantService.deleteVariantById(variantId));
	}
}
