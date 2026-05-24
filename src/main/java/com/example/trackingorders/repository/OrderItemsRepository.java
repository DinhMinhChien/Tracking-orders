package com.example.trackingorders.repository;

import com.example.trackingorders.entity.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItems,String> {

    @Query("select oi from OrderItems oi join fetch oi.products where oi.orders.id = :orderId and oi.deleted = false")
    List<OrderItems> findWithProductsByOrderId(String orderId);
}
