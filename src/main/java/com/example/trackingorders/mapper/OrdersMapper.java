package com.example.trackingorders.mapper;

import com.example.trackingorders.dto.request.CheckoutSummaryRequest;
import com.example.trackingorders.dto.request.OrdersRequest;
import com.example.trackingorders.dto.response.OrdersResponse;
import com.example.trackingorders.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {UsersMapper.class,PaymentMethodsMapper.class,PromotionsMapper.class,CarriersMapper.class,OrderItemsMapper.class})
public interface OrdersMapper {
    CheckoutSummaryRequest toCheckout(OrdersRequest ordersRequest) ;
    OrdersResponse toResponse(Orders order) ;
}
