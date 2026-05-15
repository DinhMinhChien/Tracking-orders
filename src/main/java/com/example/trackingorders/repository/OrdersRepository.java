package com.example.trackingorders.repository;

import com.example.trackingorders.common.StatusOrderEnum;
import com.example.trackingorders.entity.Orders;
import com.example.trackingorders.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,String>, JpaSpecificationExecutor<Orders> {
    @Query("""
        select coalesce(sum(o.totalPrice), 0)
        from Orders o
        where o.deleted = false
            and o.status <> :failedStatus
            and o.createdAt >= :startOfMonth
        """)
    BigDecimal sumMonthToDateRevenue(@Param("startOfMonth") LocalDateTime startOfMonth,
                                      @Param("failedStatus") StatusOrderEnum failedStatus);

    long countByDeletedFalse();

    long countByStatusAndDeletedFalse(StatusOrderEnum status);

    @Query("select o from Orders o where o.id in :orderIds and o.status = :status and o.deleted = false")
    List<Orders> findAllByIdAndStatus(List<String> orderIds, StatusOrderEnum status) ;

    @Query("select o from Orders o join fetch o.users u where o.id = :id and o.deleted = false")
    Optional<Orders> findWithUserById(String id) ;

    @Query("select o from Orders o left join fetch o.orderItems oi left join fetch oi.products join fetch o.promotions where o.id = :id and o.deleted = false")
    Optional<Orders> findWithOrderItemsAndProductAndPromotionById(String id) ;
}
