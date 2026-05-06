package com.example.trackingorders.controller;

import com.example.trackingorders.common.BaseResponse;
import com.example.trackingorders.common.StatusOrderEnum;
import com.example.trackingorders.dto.request.BulkConfirmRequest;
import com.example.trackingorders.dto.request.OrdersRequest;
import com.example.trackingorders.dto.response.OrderDashboardStats;
import com.example.trackingorders.dto.response.OrdersResponse;
import com.example.trackingorders.entity.*;
import com.example.trackingorders.repository.*;
import com.example.trackingorders.service.OrdersService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrdersController {
    private final OrdersService ordersService ;

    //http://localhost:8001/api/v1/orders
    @PostMapping
    public ResponseEntity<BaseResponse<OrdersResponse>> create(@RequestBody OrdersRequest request) {
        OrdersResponse response = ordersService.create(request) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess(response,"create success")) ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<OrdersResponse>> getDetail(@PathVariable String id) {
        OrdersResponse response = ordersService.getDetail(id) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess(response,"Lấy thành công chi tiết đơn hàng ")) ;
    }

    @GetMapping("/statistics")
    public ResponseEntity<BaseResponse<OrderDashboardStats>> getHeaderStats() {
        OrderDashboardStats response = ordersService.getHeaderStats();
        return ResponseEntity.ok(BaseResponse.ofSuccess(response,"Lấy dữ liệu thành công")) ;
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<OrdersResponse>>> getAll(@RequestParam(required = false,defaultValue = "1") int pageNumber ,
                                                            @RequestParam(required = false,defaultValue = "4") int pageSize,
                                                            @RequestParam(required = false)StatusOrderEnum status) {

        Page<OrdersResponse> orders = ordersService.getAll(pageNumber,pageSize,status);
        return ResponseEntity.ok(BaseResponse.ofSuccess(orders)) ;
    }

    @PostMapping("/test")
    public ResponseEntity<OrdersResponse> test(@RequestBody OrdersRequest ordersRequest) {
        return null ;
    }

    @PostMapping("/bulk-confirm")
    public ResponseEntity<BaseResponse<String>> bulkConfirm(@RequestBody BulkConfirmRequest request) {
        List<String> orderIds = request.getOrderIds() ;
        ordersService.bulkConfirm(orderIds) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Đã xác nhận " + orderIds.size() + " đơn hàng thành công ")) ;
    }

    @PutMapping("/{id}/pick-up")
    public ResponseEntity<BaseResponse<String>> confirmPickUp(@PathVariable String id ) {
        ordersService.confirmPickUp(id,StatusOrderEnum.PICKING) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Lấy hàng thành công")) ;
    }

    @PutMapping("/{id}/delivery-success")
    public ResponseEntity<BaseResponse<String>> confirmDeliverySuccess(@PathVariable String id) {
        ordersService.confirmDeliverySuccess(id,StatusOrderEnum.DELIVERED) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Xác nhận giao hàng thành công ")) ;
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<BaseResponse<String>> confirmOrder(@PathVariable String id) {
        return null ;
    }


}
