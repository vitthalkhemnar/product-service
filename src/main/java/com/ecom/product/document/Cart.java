package com.ecom.product.document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document(collection = "cart")
public class Cart {
	
	@Id
	private String id;

	@Indexed(unique = true)
	private String username;
	
	private List<CartItem> items = new ArrayList<>();
	
	@CreatedDate
	@Field(name = "created_at")
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	@Field(name = "updated_at")
	private LocalDateTime updatedAt;
}
