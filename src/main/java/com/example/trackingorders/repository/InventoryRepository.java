package com.example.trackingorders.repository;

import com.example.trackingorders.entity.CartItems;
import com.example.trackingorders.entity.Inventory;
import com.example.trackingorders.entity.Products;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory,String> {

    @Query("select i from Inventory i join fetch i.products p where p in :products and i.deleted = false ")
    List<Inventory> findInventoriesByProducts(List<Products> products);

    @Query("select i from Inventory i join fetch i.products p where p = :product and i.deleted = false ")
    Inventory findInventoriesByProducts(Products products);


}
