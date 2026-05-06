package com.example.trackingorders.repository;

import com.example.trackingorders.entity.Carriers;
import com.example.trackingorders.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsersRepository extends JpaRepository<Users,String> {
    Users findByUsername(String username) ;

    List<Users> findByCarrier(Carriers carrier);
}
