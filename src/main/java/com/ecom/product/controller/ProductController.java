package com.ecom.product.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.product.document.Product;
import com.ecom.product.dto.ProductRequest;
import com.ecom.product.dto.ProductResponse;
import com.ecom.product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
@Tag(name = "Product Catalog", description = "Endpoints for browsing, creating, updating, deleting, and bulk uploading products")
public class ProductController {
	
	private final ProductService productService;

	@Operation(
		summary = "Bulk import products from CSV",
		description = "Upload a CSV file containing product records to import them into the catalog. Requires ADMIN role.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> bulkUpload(
			@Parameter(description = "CSV file with product definitions", required = true)
			@RequestParam("file") MultipartFile file) {
		
		String fileName = file.getOriginalFilename();
		int index = fileName != null ? fileName.indexOf(".") : -1;
		
		if(index == -1) {
			return ResponseEntity.badRequest().body("Only files with extension .csv can be uploaded.");
		}
		
		String extention = fileName.substring(index);
		
		if(!".csv".equalsIgnoreCase(extention)) {
			return ResponseEntity.badRequest().body("Only files with extension .csv can be uploaded.");
		}
		
		productService.bulkUploadProducts(file);
		return ResponseEntity.ok().body("Products Imported Successfully.");
	}
	
	@Operation(
		summary = "Get all products",
		description = "Retrieve the full list of available products in the catalog. Public endpoint."
	)
	@GetMapping
	public ResponseEntity<List<ProductResponse>> getAllProducts() {
		List<ProductResponse> products = productService.getProducts();
		return ResponseEntity.ok().body(products);
	}
	
	@Operation(
		summary = "Get product by ID",
		description = "Retrieve detailed product information for a given product ID. Public endpoint."
	)
	@GetMapping("/{productId}")
	public ResponseEntity<ProductResponse> getProductById(
			@Parameter(description = "Unique ID of the product", required = true, example = "101")
			@PathVariable("productId") Long productId) {
		ProductResponse product = productService.getProductById(productId);
		if (product == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok().body(product);
	}
	
	@Operation(
		summary = "Get paginated products",
		description = "Retrieve a paginated and sortable list of products. Public endpoint."
	)
	@GetMapping("/page")
	public ResponseEntity<Page<Product>> getAllProducts(
			@Parameter(description = "Pagination parameters (page, size, sort)")
			@PageableDefault(size = 10, sort = "id") Pageable pageable) {
		Page<Product> products = productService.getProducts(pageable);
		return ResponseEntity.ok().body(products);
	}

	@Operation(
		summary = "Create a new product",
		description = "Add a new product entry to the catalog. Requires ADMIN role.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(
			@RequestBody ProductRequest req) {
		return ResponseEntity.ok().body(productService.createProduct(req));
	}

	@Operation(
		summary = "Update an existing product",
		description = "Update details of an existing product in the catalog. Requires ADMIN role.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@PutMapping
	public ResponseEntity<ProductResponse> updateProduct(
			@RequestBody ProductRequest req) {
		return ResponseEntity.ok().body(productService.updateProduct(req));
	}
	
	@Operation(
		summary = "Delete a product",
		description = "Permanently delete a product by its ID from the catalog. Requires ADMIN role.",
		security = @SecurityRequirement(name = "bearerAuth")
	)
	@DeleteMapping("/{productId}")
	public ResponseEntity<Boolean> deleteProduct(
			@Parameter(description = "ID of the product to delete", required = true, example = "101")
			@PathVariable("productId") Long productId) {
		return ResponseEntity.ok().body(productService.deleteProduct(productId));
	}
}
