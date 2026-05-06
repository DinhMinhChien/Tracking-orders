package com.example.trackingorders.service.implement;

import com.example.trackingorders.dto.request.CheckoutSummaryRequest;
import com.example.trackingorders.dto.response.CheckoutSummaryResponse;
import com.example.trackingorders.dto.response.PromotionsResponse;
import com.example.trackingorders.entity.CartItems;
import com.example.trackingorders.entity.Products;
import com.example.trackingorders.entity.Promotions;
import com.example.trackingorders.exception.BusinessException;
import com.example.trackingorders.mapper.PromotionsMapper;
import com.example.trackingorders.repository.CartItemsRepository;
import com.example.trackingorders.repository.ProductsRepository;
import com.example.trackingorders.repository.PromotionsRepository;
import com.example.trackingorders.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImplement implements CheckoutService {
    private final CartItemsRepository cartItemsRepository;
    private final PromotionsRepository promotionsRepository;
    private final PromotionsMapper promotionsMapper;
    private final ProductsRepository productsRepository;

    @Override
    public List<PromotionsResponse> getPromotionAvailable(CheckoutSummaryRequest request) {

        BigDecimal total = calculateCartTotal(request);
        LocalDateTime timeNow = LocalDateTime.now();
        List<Promotions> promotions = promotionsRepository.findAllByCondition(total, timeNow);
        List<PromotionsResponse> response = promotionsMapper.toResponse(promotions);
        return response;
    }

    public BigDecimal calculateCartTotal(CheckoutSummaryRequest request) {

        List<String> productIds = request.getProductIds();
        List<Integer> quantities = request.getQuantities();
        List<Products> products = productsRepository.findAllById(productIds);

        BigDecimal subTotal = new BigDecimal(0);
        for (int i = 0; i < products.size(); i++) {
            BigDecimal price = products.get(i).getPrice();
            BigDecimal quantity = new BigDecimal(quantities.get(i));
            BigDecimal itemTotal = price.multiply(quantity);
            subTotal = subTotal.add(itemTotal);
        }
        return subTotal;
    }

    @Override
    public CheckoutSummaryResponse getSummary(CheckoutSummaryRequest request) {

        BigDecimal subTotal = calculateCartTotal(request);
        String promotionId = request.getPromotionId();
        BigDecimal discountAmount = new BigDecimal(0);

        if (promotionId != null ) {
            Promotions promotion = promotionsRepository.findById(promotionId).orElseThrow(()-> new BusinessException("Promotion.fail.message"));
            if(subTotal.compareTo(promotion.getMinOrderValue()) < 0) {
                throw new BusinessException("Promotion.fail.min-order-value");
            }
            if (promotion.getDeleted() == true || promotion.getEndDate().isBefore(LocalDateTime.now())) {
                throw new BusinessException("Promotion.expired") ;
            }
            if (promotion.getUsagesLimit() == 0) {
                throw new BusinessException("Promotion.usage-limit.message");
            }
            if (promotion.getDiscountType().equalsIgnoreCase("percent")) {
                BigDecimal discountValue = new BigDecimal(promotion.getDiscountValue());
                discountAmount = subTotal.multiply(discountValue).divide(new BigDecimal(100));
            } else if (promotion.getDiscountType().equalsIgnoreCase("fixed")) {
                discountAmount = new BigDecimal(promotion.getDiscountValue());
            }

            if (discountAmount.compareTo(subTotal) > 0) {
                discountAmount = subTotal;
            }
        }

        BigDecimal shippingFee = new BigDecimal(30000);
        BigDecimal totalAmount = subTotal.subtract(discountAmount).add(shippingFee);

        CheckoutSummaryResponse response = new CheckoutSummaryResponse();
        response.setSubTotal(subTotal);
        response.setBulkDiscount(discountAmount);
        response.setExpeditedShipping(shippingFee);
        response.setTotalAmount(totalAmount);

        return response;

    }
}