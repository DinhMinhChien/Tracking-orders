package com.example.trackingorders.mapper;

import com.example.trackingorders.dto.response.PaymentMethodsResponse;
import com.example.trackingorders.entity.PaymentMethods;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMethodsMapper {
    PaymentMethodsResponse toResponse(PaymentMethods paymentMethods) ;
}
