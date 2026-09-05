package com.ecom.product.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecom.product.document.Product;
import com.ecom.product.document.Product.ProductBuilder;
import com.ecom.product.document.ProductVariant;
import com.ecom.product.dto.ProductRequest;
import com.ecom.product.dto.ProductResponse;
import com.ecom.product.repository.ProductRepository;
import com.ecom.product.repository.ProductVariantRepository;
import com.ecom.product.util.CommonUtil;
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
	private final ProductVariantRepository variantRepository;
	
	@Cacheable(value = "productService", key="'products'")
	public List<ProductResponse> getProducts() {
		List<Product> products = productRepository.findAll();
		return products.stream().map(this::mapToProductResponse).toList();
	}
	
	@Cacheable(value = "productService", key="'productsPage'")
	public Page<Product> getProducts(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	public void bulkUploadProducts(MultipartFile file) {

		try (Reader reader = new BufferedReader(
				new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));

			 CSVParser parser = CSVFormat.DEFAULT.builder()
						.setHeader()
						.setSkipHeaderRecord(true)
						.get()
						.parse(reader)
		) {
			List<Product> products = new ArrayList<>();
			List<ProductVariant> variants = new ArrayList<>();
			
			for(CSVRecord row : parser) {
				
				Product product = mapProduct(row);
				products.add(product);
				
				ProductVariant variant = mapProductVariant(row);
				variants.add(variant);
				
				if(products.size() == 500) {
					
					productRepository.saveAll(products);
					products.clear();
				}
				
				if(variants.size() == 500) {
				
					variantRepository.saveAll(variants);
					variants.clear();
				}
			}
			
			if(!products.isEmpty()) {
				productRepository.saveAll(products);
			}
						
			if(!variants.isEmpty()) {
				variantRepository.saveAll(variants);
			}
			
			log.info("Products imported successfully.");
		} catch (Exception e) {
			log.error("Importing products failed.", e);
			throw new RuntimeException(e);
		}
	}

	private Product mapProduct(CSVRecord row) {
		
		String productId = row.get("product_id");
		String variantId = row.get("variant_id");
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
		String currency = row.get("currency");
		String attributesJson = row.get("attributes_json");
		
		Map<String, Object> attributesMap = objMapper.readValue(attributesJson, Map.class);
		
		Boolean inStock = "Yes".equalsIgnoreCase(row.get("in_stock"));

		BigDecimal price = new BigDecimal(row.get("price"));
		BigDecimal discountedPrice = new BigDecimal(row.get("discounted_price"));

		Integer discount = Integer.valueOf(row.get("discount_percent"));
		Integer availability = Integer.valueOf(row.get("availability_count"));
		Integer reviews = Integer.valueOf(row.get("num_reviews"));

		Double rating = Double.valueOf(row.get("rating"));
		Double weight = Double.valueOf(row.get("weight_kg"));

		LocalDate releaseDate = LocalDate.parse(row.get("release_date"), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
		
		String imageStr = row.get("images");
		List<String> imagesList = CommonUtil.isBlank(imageStr) ? new ArrayList<String>() : Arrays.asList(imageStr.split(","));
		List<String> images = imagesList.stream().map(n -> String.valueOf(n).trim()).toList();
						
		var productBuilder = Product.builder()
			.id(Long.valueOf(productId))
			.productCode(productCode)
			.productName(productName)
			.brand(brand)
			.category(category)
			.subcategory(subcategory)
			.description(description)
			.price(price)
			.discount(discountedPrice)
			.attributes(attributesMap)
			.images(List.of())
			.status(ProductStatus.ACTIVE);
		
		if(CommonUtil.isNotBlank(material))
			productBuilder.material(material);
		
		if(!CommonUtil.isEmpty(images))
			productBuilder.images(images);
		
		return productBuilder.build();
	}
	
	private ProductVariant mapProductVariant(CSVRecord row) {
		
		String productId = row.get("product_id");
		String variantId = row.get("variant_id");
		String size = row.get("size");
		String color = row.get("color");
		String sku = row.get("sku");
		
		Boolean inStock = "Yes".equalsIgnoreCase(row.get("in_stock"));
		BigDecimal price = new BigDecimal(row.get("price"));
		Integer availability = Integer.valueOf(row.get("availability_count"));
		
		String images = row.get("images");
		List<String> imagesList = images == null ? new ArrayList<>() : Arrays.asList(images.split(","));
		imagesList.forEach(n -> String.valueOf(n).trim());
				
		ProductVariant variant = ProductVariant.builder()
			.productId(Long.valueOf(productId))
			.id(Long.valueOf(variantId))
			.active(inStock)
			.stock(availability)
			.price(price)
			.sku(sku)
			.build();
		
		if(CommonUtil.isNotBlank(color))
			variant.setColor(color);
		
		if(CommonUtil.isNotBlank(size))
			variant.setSize(size);
		
		if(!CommonUtil.isEmpty(imagesList))
			variant.setImage(imagesList.get(0));
		
		return variant;
	}

	private String getSku(String productCode, String color, String size) {
		return new StringJoiner(SkuConstants.SKU_SEPARATOR)
				.add(productCode)
				.add(getSkuCode(color, "color"))
				.add(getSkuCode(size, "size"))
				.toString();
	}
	
	private String getSkuCode(String key, String type) {
		return switch (type) {
			case "color" -> SkuConstants.COLOR.get(key) != null ? SkuConstants.COLOR.get(key) : SkuConstants.NA;
			case "size" -> SkuConstants.SIZE.get(key) != null ? SkuConstants.SIZE.get(key) : SkuConstants.NA;
			default -> SkuConstants.NA;
		};
	}
	
	public ProductResponse mapToProductResponse(Product product) {
        if (product == null) {
            return null;
        }

        return new ProductResponse(
            product.getId(),
            product.getProductCode(),
            product.getProductName(),
            product.getDescription(),
            product.getCategory(),
            product.getSubcategory(),
            product.getBrand(),
            product.getPrice(),
            product.getDiscount(),
            product.getMaterial(),
            product.getAttributes(),
            product.getImages(),
            product.getStatus()
        );
    }

	public ProductResponse updateProduct(ProductRequest req) {
		
		Product product = productRepository.findById(req.id())
				.orElseThrow(() -> new RuntimeException("Entity not found."));
		
		ProductBuilder productBuilder = product.toBuilder()
				.productCode(req.productCode())
				.productName(req.productName())
				.brand(req.brand())
				.category(req.category())
				.subcategory(req.subcategory())
				.description(req.description())
				.price(req.price())
				.discount(req.discount())
				.attributes(req.attributes())
				.images(req.images())
				.status(req.status());
			
			if(CommonUtil.isNotBlank(req.material()))
				productBuilder.material(req.material());
			
			if(!CommonUtil.isEmpty(req.images()))
				productBuilder.images(req.images());
			
			Product savedProduct = productRepository.save(productBuilder.build());
		
		return mapToProductResponse(savedProduct);
	}
	
	public boolean deleteProduct(Long productId) {
		try {
			productRepository.deleteById(productId);
			return true;
		} catch (Exception e) {
			log.error("Error while deleting prouduct with productId: {}", productId);
			return false;
		}
	}
}
