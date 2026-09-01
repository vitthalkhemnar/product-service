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
import com.ecom.product.util.CommonUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> bulkUpload(@RequestParam("file") MultipartFile file) {
		
		String fileName = file.getOriginalFilename();
		int index = fileName.indexOf(".");
		
		if(index == -1) {
			return ResponseEntity.badRequest().body("Only files with extension .csv can be uploaded.");
		}
		
		String extention = fileName.substring(index);
		
		if(!".csv".equals(extention)) {
			return ResponseEntity.badRequest().body("Only files with extension .csv can be uploaded.");
		}
		
		productService.bulkUploadProducts(file);
		return ResponseEntity.ok().body("Products Imported Successfully.");
	}
	
	@GetMapping
	public ResponseEntity<?> getAllProducts() {
		List<ProductResponse> products = productService.getProducts();
		
		if(CommonUtil.isEmpty(products))
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(products);
	}
	
	@GetMapping("/page")
	public ResponseEntity<?> getAllProducts(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
		Page<Product> products = productService.getProducts(pageable);
		return ResponseEntity.ok().body(products);
	}

	@PutMapping
	public ResponseEntity<?> updateProduct(@RequestBody ProductRequest req) {
		return ResponseEntity.ok().body(productService.updateProduct(req));
	}
	
	@DeleteMapping("/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable("productId") Long productId) {
		return ResponseEntity.ok().body(productService.deleteProduct(productId));
	}
}
