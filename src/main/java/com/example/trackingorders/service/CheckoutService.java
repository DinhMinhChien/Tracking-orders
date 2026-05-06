package com.example.trackingorders.service;

import com.example.trackingorders.dto.request.CheckoutSummaryRequest;
import com.example.trackingorders.dto.response.CheckoutSummaryResponse;
import com.example.trackingorders.dto.response.PromotionsResponse;

import java.util.List;

public interface CheckoutService {
    List<PromotionsResponse> getPromotionAvailable(CheckoutSummaryRequest request) ;
    CheckoutSummaryResponse getSummary(CheckoutSummaryRequest request) ;
}
