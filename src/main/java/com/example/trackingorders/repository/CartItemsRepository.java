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
    @Query(value = """
            update cart_items ci
            join carts c on c.id = ci.cart_id
            set ci.deleted = true
            where ci.product_id in :productIds
              and c.user_id = :userId
              and ci.deleted = false
            """, nativeQuery = true)
    void removeCartItemsByProductIdsAndUserId(List<String> productIds, String userId);

    @Query("select ci from CartItems  ci join fetch ci.products p where ci.id = :id and ci.deleted = false ")
    Optional<CartItems> findWithProductsById(String id) ;
}
