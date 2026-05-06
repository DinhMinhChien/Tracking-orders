package com.example.trackingorders.service;

import com.example.trackingorders.common.StatusOrderEnum;
import com.example.trackingorders.dto.request.OrdersRequest;
import com.example.trackingorders.dto.response.OrderDashboardStats;
import com.example.trackingorders.dto.response.OrdersResponse;
import com.example.trackingorders.entity.Orders;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrdersService {
    OrdersResponse create(OrdersRequest request) ;
    OrdersResponse getDetail(String id) ;
    OrderDashboardStats getHeaderStats() ;
    Page<OrdersResponse> getAll(int pageNumber, int pageSize, StatusOrderEnum status) ;
    void bulkConfirm(List<String> orderIds) ;
    void confirmPickUp(String id,StatusOrderEnum status) ;
    void confirmDeliverySuccess(String id,StatusOrderEnum status);
}
