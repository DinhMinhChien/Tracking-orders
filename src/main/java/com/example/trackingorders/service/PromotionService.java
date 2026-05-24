package com.example.trackingorders.service;

import com.example.trackingorders.dto.response.PromotionsResponse;
import com.example.trackingorders.entity.Orders;
import com.example.trackingorders.entity.Promotions;
import com.example.trackingorders.entity.Users;

import java.math.BigDecimal;
import java.util.List;

public interface PromotionService {
    List<PromotionsResponse> getAvailablePromotions(BigDecimal orderTotal);
    Promotions validatePromotion(String promotionId, BigDecimal subTotal);
    BigDecimal calculateDiscount(Promotions promotion, BigDecimal subTotal);
    BigDecimal calculateDiscountAmount(String promotionId, BigDecimal subTotal);
    void usePromotion(Users user, Promotions promotion, Orders order);
    void restorePromotion(Orders order) ;
}
