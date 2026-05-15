package com.example.trackingorders.repository;

import com.example.trackingorders.entity.Promotions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PromotionsRepository extends JpaRepository<Promotions,String> {

    @Query("select p from Promotions p where p.minOrderValue <= :total and p.startDate <= :timeNow and p.endDate >= :timeNow and p.usagesLimit > 0 and p.deleted = false ")
    List<Promotions> findAllByCondition(BigDecimal total, LocalDateTime timeNow) ;
}
