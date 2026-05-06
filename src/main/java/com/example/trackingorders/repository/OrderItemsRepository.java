package com.example.trackingorders.repository;

import com.example.trackingorders.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemsRepository extends JpaRepository<OrderItems,String> {
}
