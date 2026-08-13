package com.ecom.product.util;

import java.util.Map;

/**
 * Centralized SKU constants for the product catalog.
 *
 * SKU format:
 * CATEGORY-SUBCATEGORY-BRAND-PRODUCT_ID-COLOR-SIZE
 *
 * Example:
 * CLO-TSH-ALS-100011-BK-M
 *
 * NA means that the attribute is not applicable to the product.
 */
public final class SkuConstants {

    private SkuConstants() {}

    public static final String NA = "NA";
    public static final String SKU_SEPARATOR = "-";
    public static final String SKU_FORMAT =
            "CATEGORY-SUBCATEGORY-BRAND-COLOR-SIZE-PRODUCT_ID";

    public static final Map<String, String> CATEGORY = Map.ofEntries(
        Map.entry("Beauty & Personal Care", "BPC"),
        Map.entry("Books", "BOK"),
        Map.entry("Clothing", "CLO"),
        Map.entry("Electronics", "ELE"),
        Map.entry("Footwear", "FTW"),
        Map.entry("Home & Kitchen", "HNK"),
        Map.entry("Sports & Fitness", "SPF")
    );

    public static final Map<String, String> SUBCATEGORY = Map.ofEntries(
        Map.entry("Bedsheet", "BED"),
        Map.entry("Biography", "BIO"),
        Map.entry("Bluetooth Speaker", "BTS"),
        Map.entry("Boots", "BOT"),
        Map.entry("Business", "BUS"),
        Map.entry("Children", "CHD"),
        Map.entry("Cookware", "CKW"),
        Map.entry("Cricket Bat", "CRB"),
        Map.entry("Curtains", "CUR"),
        Map.entry("Dumbbells", "DMB"),
        Map.entry("Fiction", "FIC"),
        Map.entry("Football", "FBL"),
        Map.entry("Formal Shoes", "FMS"),
        Map.entry("Fragrance", "FRG"),
        Map.entry("Haircare", "HRC"),
        Map.entry("Headphones", "HPH"),
        Map.entry("Jacket", "JKT"),
        Map.entry("Jeans", "JNS"),
        Map.entry("Kitchen Appliance", "KAP"),
        Map.entry("Kurta", "KUR"),
        Map.entry("Laptop", "LAP"),
        Map.entry("Makeup", "MUP"),
        Map.entry("Non-Fiction", "NFC"),
        Map.entry("Personal Grooming", "GRM"),
        Map.entry("Power Bank", "PWB"),
        Map.entry("Resistance Bands", "RBD"),
        Map.entry("Sandals", "SDL"),
        Map.entry("Self-Help", "SLF"),
        Map.entry("Shirt", "SHT"),
        Map.entry("Skincare", "SKC"),
        Map.entry("Smartphone", "SPH"),
        Map.entry("Smartwatch", "SMW"),
        Map.entry("Sneakers", "SNK"),
        Map.entry("Sports Shoes", "SPS"),
        Map.entry("Storage & Containers", "STC"),
        Map.entry("T-Shirt", "TSH"),
        Map.entry("Yoga Mat", "YGM")
    );

    public static final Map<String, String> BRAND = Map.ofEntries(
        Map.entry("Adidas", "ADI"),
        Map.entry("Allen Solly", "ALS"),
        Map.entry("Apple", "APP"),
        Map.entry("Bata", "BAT"),
        Map.entry("Bloomsbury", "BLO"),
        Map.entry("Boldfit", "BOL"),
        Map.entry("Bombay Dyeing", "BMD"),
        Map.entry("Borosil", "BOR"),
        Map.entry("Campus", "CMP"),
        Map.entry("Cello", "CEL"),
        Map.entry("Cosco", "COS"),
        Map.entry("Crocs", "CRC"),
        Map.entry("Dell", "DEL"),
        Map.entry("Dove", "DOV"),
        Map.entry("Fabindia", "FAB"),
        Map.entry("H&M", "HNM"),
        Map.entry("HP", "HPP"),
        Map.entry("HarperCollins", "HCL"),
        Map.entry("JBL", "JBL"),
        Map.entry("Kobo", "KOB"),
        Map.entry("Lakme", "LAK"),
        Map.entry("Lenovo", "LNV"),
        Map.entry("Levis", "LEV"),
        Map.entry("Mamaearth", "MAM"),
        Map.entry("Maybelline", "MAY"),
        Map.entry("Milton", "MLT"),
        Map.entry("Nike", "NIK"),
        Map.entry("Nivea", "NIV"),
        Map.entry("Nivia", "NVA"),
        Map.entry("Noise", "NOI"),
        Map.entry("OnePlus", "ONP"),
        Map.entry("Penguin", "PNG"),
        Map.entry("Peter England", "PEN"),
        Map.entry("Pigeon", "PIG"),
        Map.entry("Prestige", "PRS"),
        Map.entry("Puma", "PUM"),
        Map.entry("Random House", "RHO"),
        Map.entry("Realme", "RLM"),
        Map.entry("Red Tape", "RDT"),
        Map.entry("Reebok", "RBK"),
        Map.entry("Roadster", "RDS"),
        Map.entry("Rupa Publications", "RUP"),
        Map.entry("SG", "SG"),
        Map.entry("Samsung", "SAM"),
        Map.entry("Simon & Schuster", "SAS"),
        Map.entry("Skechers", "SKR"),
        Map.entry("Sony", "SNY"),
        Map.entry("The Ordinary", "ORD"),
        Map.entry("US Polo", "USP"),
        Map.entry("Van Heusen", "VHS"),
        Map.entry("Wonderchef", "WCH"),
        Map.entry("Woodland", "WDL"),
        Map.entry("Wow Skin Science", "WOW"),
        Map.entry("Xiaomi", "XIA"),
        Map.entry("Yonex", "YON"),
        Map.entry("Zara", "ZAR"),
        Map.entry("boAt", "BOT")
    );

    public static final Map<String, String> COLOR = Map.ofEntries(
        Map.entry("Beige", "BE"),
        Map.entry("Black", "BK"),
        Map.entry("Charcoal", "CH"),
        Map.entry("Grey", "GY"),
        Map.entry("Maroon", "MR"),
        Map.entry("Mustard", "MU"),
        Map.entry("Navy Blue", "NB"),
        Map.entry("Olive Green", "OG"),
        Map.entry("Pink", "PK"),
        Map.entry("Red", "RD"),
        Map.entry("Sky Blue", "SB"),
        Map.entry("White", "WH")
    );

    public static final Map<String, String> SIZE = Map.ofEntries(
        Map.entry("XS", "XS"),
        Map.entry("S", "S"),
        Map.entry("M", "M"),
        Map.entry("L", "L"),
        Map.entry("XL", "XL"),
        Map.entry("XXL", "XXL"),
        Map.entry("6", "06"),
        Map.entry("7", "07"),
        Map.entry("8", "08"),
        Map.entry("9", "09"),
        Map.entry("10", "10"),
        Map.entry("11", "11"),
        Map.entry("12", "12")
    );

    public static final Map<String, String> MATERIAL = Map.ofEntries(
        Map.entry("Canvas", "CAN"),
        Map.entry("Cotton", "COT"),
        Map.entry("Cotton Blend", "CBL"),
        Map.entry("Denim", "DEN"),
        Map.entry("EVA", "EVA"),
        Map.entry("Genuine Leather", "GNL"),
        Map.entry("Knit Fabric", "KNT"),
        Map.entry("Leather", "LEA"),
        Map.entry("Linen", "LIN"),
        Map.entry("Mesh", "MSH"),
        Map.entry("Nylon", "NYL"),
        Map.entry("Polyester", "POL"),
        Map.entry("Rayon", "RAY"),
        Map.entry("Rubber", "RUB"),
        Map.entry("Silk Blend", "SBL"),
        Map.entry("Stretch Denim", "SDN"),
        Map.entry("Suede", "SUE"),
        Map.entry("Synthetic Leather", "SYN"),
        Map.entry("Wool Blend", "WBL")
    );
}
