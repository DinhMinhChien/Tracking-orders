package com.example.trackingorders.repository;

import com.example.trackingorders.common.StatusReturnEnum;
import com.example.trackingorders.dto.response.ReturnSummaryResponse;
import com.example.trackingorders.entity.Returns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReturnsRepository extends JpaRepository<Returns,String>, JpaSpecificationExecutor<Returns> {
    @Query(value = """
    SELECT
        SUM(CASE WHEN r.status = 'ACTIVE' THEN 1 ELSE 0 END) AS activeReturns,
        SUM(CASE WHEN r.status = 'AWAITING_INSPECTION' THEN 1 ELSE 0 END) AS awaitingInspection,
        COALESCE(SUM(CASE 
            WHEN r.status = 'REFUNDED'
             AND QUARTER(r.created_at) = QUARTER(CURDATE())
             AND YEAR(r.created_at) = YEAR(CURDATE())
            THEN o.total_price
            ELSE 0
        END), 0) AS totalRefunds
    FROM returns r
    JOIN orders o ON r.order_id = o.id
    WHERE r.deleted = 0
    """, nativeQuery = true)
    ReturnSummaryResponse getDashboardStats();

    List<Returns> findByDeletedFalse();

    List<Returns> findByStatusAndDeletedFalse(StatusReturnEnum status);
}
