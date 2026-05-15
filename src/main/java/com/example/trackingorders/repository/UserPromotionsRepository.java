package com.example.trackingorders.repository;

import com.example.trackingorders.entity.Promotions;
import com.example.trackingorders.entity.UserPromotions;
import com.example.trackingorders.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPromotionsRepository extends JpaRepository<UserPromotions,String> {

    boolean existsByUsersAndPromotionsAndDeletedFalse(Users users, Promotions promotions) ;

    UserPromotions findByPromotions(Promotions promotion) ;
}
