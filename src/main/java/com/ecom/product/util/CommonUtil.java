package com.ecom.product.util;

import java.util.Collection;

public class CommonUtil {

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
