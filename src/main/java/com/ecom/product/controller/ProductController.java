package com.ecom.product.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> bulkUpload(@RequestParam("file") MultipartFile file) {
		return productService.bulkUploadProducts(file);
	}
	
	@GetMapping
	public ResponseEntity<?> getAllProducts() {
		return productService.getProducts();
	}
	
	@GetMapping("/page")
	public ResponseEntity<?> getAllProducts(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
		return productService.getProducts(pageable);
	}

}
