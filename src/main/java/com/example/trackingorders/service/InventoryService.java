package com.example.trackingorders.service;

import com.example.trackingorders.entity.Products;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface InventoryService {
    void decreaseStock(List<Products> products, Map<String, Integer> quantityByProductId) ;
    void restoreQuantityProduct(List<Products> products,Map<String,Integer> quantityByProductId);
}
