package com.example.trackingorders.repository;

import com.example.trackingorders.common.StatusOrderEnum;
import com.example.trackingorders.dto.response.OrderDashboardStats;
import com.example.trackingorders.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders,String>, JpaSpecificationExecutor<Orders> {
    @Query(value = """
        SELECT 
            SUM(CASE WHEN status != 'FAILED' 
                     AND created_at >= DATE_TRUNC('month', CURRENT_DATE) 
                THEN amount ELSE 0 END) AS mtdRevenue,
            COUNT(*) AS totalOrders,
            COUNT(CASE WHEN status = 'PENDING' THEN 1 END) AS pendingOrders,
            COUNT(CASE WHEN status = 'SHIPPING' THEN 1 END) AS shippingOrders,
            COUNT(CASE WHEN status = 'FAILED' THEN 1 END) AS failedOrders
        FROM orders
        """, nativeQuery = true)
    OrderDashboardStats getDashboardStatistics();

    @Query("select o from Orders o where o.id in :orderIds and o.status = :status and o.deleted = false")
    List<Orders> findALLByIdAndStatus(List<String> orderIds, StatusOrderEnum status) ;
}
