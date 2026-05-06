package com.example.trackingorders.repository;

import com.example.trackingorders.entity.CartItems;
import com.example.trackingorders.entity.Inventory;
import com.example.trackingorders.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory,String> {

    @Modifying
    @Transactional
    @Query("update Inventory i set i.quantity = i.quantity - :quantity where i.products = :product")
    void decreaseStockQuantity(Products product,Integer quantity);

    @Query("select i from Inventory i where i.products in :products")
    List<Inventory> findInventoriesByProducts(List<Products> products) ;

    Inventory findInventoriesByProducts(Products products);
}
