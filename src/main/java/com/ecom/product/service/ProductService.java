package com.ecom.product.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.product.document.Product;
import com.ecom.product.document.ProductVariant;
import com.ecom.product.repository.ProductRepository;
import com.ecom.product.util.ProductStatus;
import com.ecom.product.util.SkuConstants;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

	private final ObjectMapper objMapper;
	private final ProductRepository productRepository;
	
	public ResponseEntity<?> getProducts() {
		List<Product> products = productRepository.findAll();
		
		return ResponseEntity.ok().body(products.stream().limit(10).toList());
	}
	
	public ResponseEntity<?> getProducts(Pageable pageable) {
		Page<Product> products = productRepository.findAll(pageable);
		
		return ResponseEntity.ok().body(products);
	}

	public ResponseEntity<?> bulkUploadProducts(MultipartFile file) {

		try (Reader reader = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));

			 CSVParser parser = CSVFormat.DEFAULT.builder()
						.setHeader()
						.setSkipHeaderRecord(true)
						.get()
						.parse(reader)
		) {
			List<Product> products = new ArrayList<>();
			
			for(CSVRecord row : parser) {
				
				Product product = mapProduct(row);
				products.add(product);
				
				if(products.size() == 500) {
					
					productRepository.saveAll(products);
					products.clear();
				}
			}
			
			if(!products.isEmpty()) {
				productRepository.saveAll(products);
			}

		} catch (Exception e) {
			log.error("", e);
			return ResponseEntity.internalServerError().body(e);
		}
		return ResponseEntity.ok().body("Products Imported Successfully.");
	}

	private Product mapProduct(CSVRecord row) {
		
		String productId = row.get("product_id");
		String productCode = row.get("product_code");
		String productName = row.get("product_name");
		String category = row.get("category");
		String subcategory = row.get("subcategory");
		String brand = row.get("brand");
		String description = row.get("description");

		String warehouse = row.get("warehouse_location");
		String size = row.get("size");
		String color = row.get("color");
		String material = row.get("material");
		
		String dimensions = row.get("dimensions_cm_lwh");
		String attributesJson = row.get("attributes_json");
		Map<String, Object> attributesMap = objMapper.readValue(attributesJson, Map.class);
		
		String currency = row.get("currency");
		
		Boolean inStock = "Yes".equalsIgnoreCase(row.get("in_stock"));

		BigDecimal price = new BigDecimal(row.get("price"));
		BigDecimal discountedPrice = new BigDecimal(row.get("discounted_price"));

		Integer discount = Integer.valueOf(row.get("discount_percent"));
		Integer availability = Integer.valueOf(row.get("availability_count"));
		Integer reviews = Integer.valueOf(row.get("num_reviews"));

		Double rating = Double.valueOf(row.get("rating"));
		Double weight = Double.valueOf(row.get("weight_kg"));

		LocalDate releaseDate = LocalDate.parse(row.get("release_date"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		
		String sku = getSku(productCode, color, size);
				
		ProductVariant productVariant = ProductVariant.builder()
			.active(inStock)
			.color(color)
			.size(size)
			.stock(availability)
			.price(price)
			.sku(sku)
			.build();
				
		Optional<Product> productOpt = productRepository.findById(Long.valueOf(productId));
		
		List<ProductVariant> variants = new ArrayList<>();
		if(productOpt.isPresent()) {
			variants = productOpt.get().getVariants();
			
			Optional<ProductVariant> existingProdOpt = variants.stream().filter(p -> p.getSku().equals(productVariant.getSku())).findFirst();
			
			if(existingProdOpt.isPresent()) {
				ProductVariant variant = existingProdOpt.get();
				variant.setStock(variant.getStock() + productVariant.getStock());
			} else {
				variants.add(productVariant);
			}
		} else {
			variants.add(productVariant);
		}
						
		return Product.builder()
			.id(Long.valueOf(productId))
			.productCode(productCode)
			.productName(productName)
			.brand(brand)
			.category(category)
			.subcategory(subcategory)
			.description(description)
			.price(price)
			.discount(discountedPrice)
			.material(material)
			.attributes(attributesMap)
			.images(List.of())
			.status(ProductStatus.ACTIVE)
			.variants(variants)
			.build();
	}

	private String getSku(String productCode, String color, String size) {
		return new StringJoiner(SkuConstants.SKU_SEPARATOR)
				.add(productCode)
				.add(getSkuCode(color))
				.add(getSkuCode(size))
				.toString();
	}
	
	private String getSkuCode(String key) {
		return SkuConstants.COLOR.get(key) != null ? SkuConstants.COLOR.get(key) : SkuConstants.NA;
	}
}
