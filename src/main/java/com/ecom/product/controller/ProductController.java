package com.ecom.product.controller;

import java.util.List;

import org.springframework.data.domain.Page;
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

import com.ecom.product.document.Product;
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
		List<Product> products = productService.getProducts();
		
		if(CommonUtil.isEmpty(products))
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok().body(products);
	}
	
	@GetMapping("/page")
	public ResponseEntity<?> getAllProducts(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
		Page<Product> products = productService.getProducts(pageable);
		return ResponseEntity.ok().body(products);
	}

}
