package com.example.trackingorders.repository;

import com.example.trackingorders.entity.Addresses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressesRepository extends JpaRepository<Addresses,String> {
    @Query("select a from Addresses a join a.users u where u.username = :username")
    List<Addresses> findByUserName(String username) ;
}
