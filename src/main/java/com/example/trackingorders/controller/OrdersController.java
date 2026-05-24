package com.example.trackingorders.controller;

import com.example.trackingorders.common.BaseResponse;
import com.example.trackingorders.common.StatusOrderEnum;
import com.example.trackingorders.dto.request.BulkConfirmRequest;
import com.example.trackingorders.dto.request.OrdersRequest;
import com.example.trackingorders.dto.response.OrderDetailResponse;
import com.example.trackingorders.dto.response.OrderDashboardStats;
import com.example.trackingorders.dto.response.OrderListResponse;
import com.example.trackingorders.service.OrdersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Validated
public class OrdersController {
    private final OrdersService ordersService ;
    private final MessageSource messageSource ;

    //http://localhost:8001/api/v1/orders
    @PostMapping
    public ResponseEntity<BaseResponse<OrderDetailResponse>> create(@RequestBody @Valid OrdersRequest request) {
        OrderDetailResponse response = ordersService.create(request) ;
        String message = messageSource.getMessage(
                "Order-create.message",
                null,
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.ok(BaseResponse.ofSuccess(response,message));
    }

    //http://localhost:8001/api/v1/orders/{id}
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<OrderDetailResponse>> getDetail(@PathVariable String id) {
        OrderDetailResponse response = ordersService.getDetail(id) ;
        String message = messageSource.getMessage(
                "Order-detail.message",
                null,
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.ok(BaseResponse.ofSuccess(response, message)) ;
    }

    //http://localhost:8001/api/v1/orders/statistics
    @GetMapping("/statistics")
    public ResponseEntity<BaseResponse<OrderDashboardStats>> getHeaderStats() {
        OrderDashboardStats response = ordersService.getHeaderStats();
        String message = messageSource.getMessage(
                "Order-statistics.message",
                null,
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.ok(BaseResponse.ofSuccess(response,message)) ;
    }

    //http://localhost:8001/api/v1/orders
    @GetMapping
    public ResponseEntity<BaseResponse<List<OrderListResponse>>> getAll(@RequestParam(required = false,defaultValue = "1") int pageNumber ,
                                                                        @RequestParam(required = false,defaultValue = "4") int pageSize,
                                                                        @RequestParam(required = false)StatusOrderEnum status) {

        Page<OrderListResponse> orders = ordersService.getAll(pageNumber,pageSize,status);
        return ResponseEntity.ok(BaseResponse.ofSuccess(orders)) ;
    }

    //http://localhost:8001/api/v1/orders/bulk-confirm

    @PostMapping("/bulk-confirm")
    public ResponseEntity<BaseResponse<String>> bulkConfirm(@RequestBody @Valid BulkConfirmRequest request) {
        List<String> orderIds = request.getOrderIds() ;
        ordersService.bulkConfirm(orderIds) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Confirmed " + orderIds.size() + " order success ")) ;
    }
    //http://localhost:8001/api/v1/orders/{id}/confirm
    @PutMapping("/{id}/confirm")
    public ResponseEntity<BaseResponse<String>> confirmOrder(@PathVariable String id) {
        ordersService.confirmOrder(id) ;
        String message = messageSource.getMessage(
                "Order-confirm.message",
                null,
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.ok(BaseResponse.ofSuccess(message)) ;
    }

    //http://localhost:8001/api/v1/orders/{id}/reject
    @PutMapping("/{id}/reject")
    public ResponseEntity<BaseResponse<String>> rejectOrder(@PathVariable String id,@RequestBody String reason) {
        ordersService.rejectOrder(id,reason) ;
        String message = messageSource.getMessage(
                "Order-reject.message",
                null,
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.ok(BaseResponse.ofSuccess(message)) ;
    }

    //http://localhost:8001/api/v1/orders/{id}/pick-up
    @PutMapping("/{id}/pick-up")
    public ResponseEntity<BaseResponse<String>> confirmPickUp(@PathVariable String id ) {
        ordersService.confirmPickUp(id,StatusOrderEnum.PICKING) ;
        String message = messageSource.getMessage(
                "Order-pickup.message",
                null,
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.ok(BaseResponse.ofSuccess(message)) ;
    }

    //http://localhost:8001/api/v1/orders/{id}/shipping
    @PutMapping("/{id}/shipping")
    public ResponseEntity<BaseResponse<String>> confirmShipping(@PathVariable String id) {
        ordersService.confirmShipping(id,StatusOrderEnum.SHIPPING) ;
        String message = messageSource.getMessage(
                "Order-shipping.message",
                null,
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.ok(BaseResponse.ofSuccess(message));
    }


    //http://localhost:8001/api/v1/orders/{id}/delivery-success
    @PutMapping("/{id}/delivery-success")
    public ResponseEntity<BaseResponse<String>> confirmDeliverySuccess(@PathVariable String id) {
        ordersService.confirmDeliverySuccess(id,StatusOrderEnum.DELIVERED) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Delivery success")) ;
    }



}
