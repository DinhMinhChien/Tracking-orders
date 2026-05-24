package com.example.trackingorders.repository;

import com.example.trackingorders.common.StatusReturnEnum;
import com.example.trackingorders.dto.response.ReturnSummaryProjection;
import com.example.trackingorders.entity.Returns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReturnsRepository extends JpaRepository<Returns,String>, JpaSpecificationExecutor<Returns> {
    @Query(value = """
        SELECT
            COALESCE(SUM(CASE WHEN r.status IN (:activeStatuses) THEN 1 ELSE 0 END), 0) AS activeReturns,
            COALESCE(SUM(CASE WHEN r.status = :awaitingInspectionStatus THEN 1 ELSE 0 END), 0) AS awaitingInspection,
            COALESCE(SUM(CASE WHEN r.status = :refundedStatus
                    AND r.created_at >= :startOfQuarter
                THEN o.total_price ELSE 0 END), 0) AS totalRefunds
        FROM returns r
        JOIN orders o ON r.order_id = o.id
        WHERE r.deleted = false
        """, nativeQuery = true)
    ReturnSummaryProjection getDashboardStats(@Param("activeStatuses") List<String> activeStatuses,
                                              @Param("awaitingInspectionStatus") String awaitingInspectionStatus,
                                              @Param("refundedStatus") String refundedStatus,
                                              @Param("startOfQuarter") LocalDateTime startOfQuarter);


    @Query("select r from Returns r join fetch r.orders o join fetch o.users where r.id = :id")
    Optional<Returns> findWithOrderAndUserById(String id) ;

    @Query("SELECT r FROM Returns r " +
            "LEFT JOIN FETCH r.orders o " +
            "LEFT JOIN FETCH o.users u " +
            "WHERE (:status IS NULL OR r.status = :status) " +
            "AND r.deleted = false")
    List<Returns> findAllForExport(@Param("status") StatusReturnEnum status);

    @Query("select r from Returns r join fetch r.orders o join fetch o.orderItems oi join fetch oi.products left join fetch o.promotions where r.id = :returnId")
    Optional<Returns> findWithOrderItemsAndProductAndPromotion(String returnId);

}
