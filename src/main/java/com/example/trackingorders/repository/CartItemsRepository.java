package com.example.trackingorders.repository;

import com.example.trackingorders.entity.CartItems;
import com.example.trackingorders.entity.Carts;
import com.example.trackingorders.entity.Products;
import com.example.trackingorders.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemsRepository extends JpaRepository<CartItems,String> {

    @Query("select ci from CartItems ci join fetch ci.products p join ci.carts c join c.users u where u.username = :username and ci.deleted = false ")
    List<CartItems> findCartItemsByUsername(String username) ;

    @Modifying
    @Query("update CartItems ci set ci.deleted = true where ci.products in :products and ci.carts.users = :user and ci.deleted = false ")
    void removeCartItemsByProducts(List<Products> products, Users user);

    @Query("select ci from CartItems  ci join fetch ci.products p where ci.id = :id and ci.deleted = false ")
    Optional<CartItems> findWithProductsById(String id) ;
}
