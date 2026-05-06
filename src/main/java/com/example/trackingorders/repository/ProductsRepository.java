package com.example.trackingorders.repository;

import com.example.trackingorders.dto.response.ProductDashboardStatis;
import com.example.trackingorders.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products,String>, JpaSpecificationExecutor<Products> {
    @Query("select i.quantity from Products p left join Inventory i on p = i.products where p in :products")
    List<Integer> findQuantityProduct(List<Products> products) ;

    @Query("select i.quantity from Products p left join Inventory i on p = i.products where p = :product")
    Integer findQuantity(Products product) ;

    @Query("SELECT " +
            "SUM(p.price * i.quantity) as totalInventoryValue, " +
            "COUNT(p.id) as totalProductCount, " +
            "SUM(CASE WHEN i.quantity <= 10 THEN 1 ELSE 0 END) as lowStockCount " +
            "FROM Products p join Inventory  i")
    ProductDashboardStatis getInventoryDashboardStats();
}
