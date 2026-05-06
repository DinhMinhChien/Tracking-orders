package com.example.trackingorders.repository;

import com.example.trackingorders.entity.CartItems;
import com.example.trackingorders.entity.Carts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemsRepository extends JpaRepository<CartItems,String> {
    List<CartItems> findByCarts(Carts carts) ;

    @Query("select ci from CartItems ci join ci.carts c join c.users u where u.username = :username")
    List<CartItems> findCartItemsByUsername(String username) ;
}
