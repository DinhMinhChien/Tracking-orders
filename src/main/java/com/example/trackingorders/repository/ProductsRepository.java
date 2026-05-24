package com.example.trackingorders.repository;

import com.example.trackingorders.dto.response.ProductDashboardStats;
import com.example.trackingorders.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ProductsRepository extends JpaRepository<Products,String>, JpaSpecificationExecutor<Products> {

    @Query("SELECT new com.example.trackingorders.dto.response.ProductDashboardStats(" +
            "SUM(p.price * i.quantity) as totalInventoryValue, " +
            "COUNT(p.id) as totalProductCount, " +
            "SUM(CASE WHEN i.quantity <= 10 THEN 1 ELSE 0 END) as lowStockCount) " +
            "FROM Inventory i JOIN i.products p")
    ProductDashboardStats getInventoryDashboardStats();

}
