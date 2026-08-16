package com.ecom.product.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ecom.product.document.ProductVariant;


@Repository
public interface ProductVariantRepository extends MongoRepository<ProductVariant, Long> {

	List<ProductVariant> findByProductId(Long productId);
}
