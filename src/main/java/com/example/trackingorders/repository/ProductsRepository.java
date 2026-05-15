package com.example.trackingorders.repository;

import com.example.trackingorders.dto.response.ProductDashboardStats;
import com.example.trackingorders.entity.Orders;
import com.example.trackingorders.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductsRepository extends JpaRepository<Products,String>, JpaSpecificationExecutor<Products> {

    @Query("SELECT " +
            "SUM(p.price * i.quantity) as totalInventoryValue, " +
            "COUNT(p.id) as totalProductCount, " +
            "SUM(CASE WHEN i.quantity <= 10 THEN 1 ELSE 0 END) as lowStockCount " +
            "FROM Products p join Inventory  i")
    ProductDashboardStats getInventoryDashboardStats();

}
