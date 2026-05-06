package com.example.trackingorders.repository;

import com.example.trackingorders.dto.response.CartItemResponse;
import com.example.trackingorders.entity.CartItems;
import com.example.trackingorders.entity.Carts;
import com.example.trackingorders.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartsRepository extends JpaRepository<Carts,String> {

    @Query("select ci from Carts c " +
            "join c.users u " +
            "join c.cartItems ci " +
            "join ci.products p " +
            "where u.username = :username and ci.deleted = false ")
    List<CartItems> findCartsByUsername(String username) ;
}
