package com.example.trackingorders.repository;

import com.example.trackingorders.entity.Promotions;
import com.example.trackingorders.entity.UserPromotions;
import com.example.trackingorders.entity.Users;
import com.example.trackingorders.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPromotionsRepository extends JpaRepository<UserPromotions,String> {

    boolean existsByUsersAndPromotionsAndDeletedFalse(Users users, Promotions promotions) ;

    UserPromotions findByPromotions(Promotions promotion) ;

    Optional<UserPromotions> findByOrdersAndDeletedFalse(Orders order) ;
}
