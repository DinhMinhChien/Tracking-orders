package com.example.trackingorders.repository;

import com.example.trackingorders.entity.PaymentMethods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodsRepository extends JpaRepository<PaymentMethods,String> {
    PaymentMethods findPaymentMethodsByType(String type) ;
}
