package com.example.trackingorders.controller;

import com.example.trackingorders.common.BaseResponse;
import com.example.trackingorders.dto.request.CheckoutSummaryRequest;
import com.example.trackingorders.dto.response.CheckoutSummaryResponse;
import com.example.trackingorders.dto.response.PromotionsResponse;
import com.example.trackingorders.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService ;

    //http://localhost:8001/api/v1/checkout/promotions-available
    @PostMapping("/promotions-available")
    public ResponseEntity<BaseResponse<List<PromotionsResponse>>> getPromotionAvailable(@RequestBody CheckoutSummaryRequest request) {
        List<PromotionsResponse> promotionsResponses = checkoutService.getPromotionAvailable(request) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess(promotionsResponses,"LoadPromotionAvailable.message")) ;
    }

    //http://localhost:8001/api/v1/checkout/summary
    @PostMapping("/summary")
    public ResponseEntity<BaseResponse<CheckoutSummaryResponse>> getSummary(@RequestBody CheckoutSummaryRequest request) {
        CheckoutSummaryResponse response = checkoutService.getSummary(request) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess(response,"SummaryCart.message")) ;
    }
}
