package com.example.trackingorders.util;

import com.example.trackingorders.exception.BusinessException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ProductQuantityUtils {
    private ProductQuantityUtils() {
    }

    public static Map<String, Integer> toQuantityByProductId(List<String> productIds, List<Integer> quantities) {
        if (productIds == null || quantities == null || productIds.size() != quantities.size()) {
            throw new BusinessException("Product ids and quantities size mismatch") ;
        }

        Map<String, Integer> quantityByProductId = new HashMap<>() ;
        for (int i = 0; i < productIds.size(); i++) {
            quantityByProductId.merge(productIds.get(i), quantities.get(i), Integer::sum) ;
        }
        return quantityByProductId ;
    }

    public static void validateAllProductsFound(List<?> products, Map<String, Integer> quantityByProductId) {
        if (products.size() != quantityByProductId.size()) {
            throw new BusinessException("Some products not found") ;
        }
    }
}
