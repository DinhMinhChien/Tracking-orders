package com.example.trackingorders.service;

import com.example.trackingorders.common.StatusOrderEnum;
import com.example.trackingorders.dto.request.OrdersRequest;
import com.example.trackingorders.dto.response.OrderDetailResponse;
import com.example.trackingorders.dto.response.OrderDashboardStats;
import com.example.trackingorders.dto.response.OrderListResponse;
import com.example.trackingorders.entity.Orders;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrdersService {
    OrderDetailResponse create(OrdersRequest request) ;
    OrderDetailResponse getDetail(String id) ;
    OrderDashboardStats getHeaderStats() ;
    Page<OrderListResponse> getAll(int pageNumber, int pageSize, StatusOrderEnum status) ;
    void bulkConfirm(List<String> orderIds) ;
    void confirmPickUp(String id,StatusOrderEnum status) ;
    void confirmDeliverySuccess(String id,StatusOrderEnum status);
    void confirmOrder(String id) ;
    void rejectOrder(String id,String reason) ;
    void confirmShipping(String id , StatusOrderEnum status) ;
}
