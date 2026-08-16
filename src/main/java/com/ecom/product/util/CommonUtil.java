package com.ecom.product.util;

public class CommonUtil {

	public static boolean isBlank(String str) {
		return str == null || str.isBlank();
	}
	
	public static boolean isNotBlank(String str) {
		return str != null && !str.isBlank();
	}
}
