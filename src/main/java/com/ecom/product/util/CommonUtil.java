package com.ecom.product.util;

import java.util.Collection;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {
	
	public static String getCurrentUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	public static boolean isBlank(String str) {
		return str == null || str.isBlank();
	}
	
	public static boolean isNotBlank(String str) {
		return str != null && !str.isBlank();
	}
	
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}
}
