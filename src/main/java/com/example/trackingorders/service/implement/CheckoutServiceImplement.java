package com.example.trackingorders.service.implement;

import com.example.trackingorders.dto.request.CheckoutSummaryRequest;
import com.example.trackingorders.dto.response.CheckoutSummaryResponse;
import com.example.trackingorders.dto.response.PromotionsResponse;
import com.example.trackingorders.entity.Products;
import com.example.trackingorders.repository.ProductsRepository;
import com.example.trackingorders.service.CheckoutService;
import com.example.trackingorders.service.PromotionService;
import com.example.trackingorders.util.ProductQuantityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CheckoutServiceImplement implements CheckoutService {
    private final ProductsRepository productsRepository;
    private final PromotionService promotionService ;

    @Override
    @Transactional(readOnly = true)
    public List<PromotionsResponse> getPromotionAvailable(CheckoutSummaryRequest request) {

        BigDecimal subTotal = calculateCartTotal(request);
        List<PromotionsResponse> responses = promotionService.getAvailablePromotions(subTotal) ;
        return responses;
    }

    public BigDecimal calculateCartTotal(CheckoutSummaryRequest request) {

        List<String> productIds = request.getProductIds();
        List<Integer> quantities = request.getQuantities();
        Map<String, Integer> quantityByProductId = ProductQuantityUtils.toQuantityByProductId(productIds, quantities);
        List<Products> products = productsRepository.findAllById(productIds);
        ProductQuantityUtils.validateAllProductsFound(products, quantityByProductId);

        BigDecimal subTotal = new BigDecimal(0);
        for (Products product : products) {
            BigDecimal price = product.getPrice();
            BigDecimal quantity = new BigDecimal(quantityByProductId.get(product.getId()));
            BigDecimal itemTotal = price.multiply(quantity);
            subTotal = subTotal.add(itemTotal);
        }
        return subTotal;
    }

    @Override
    @Transactional(readOnly = true)
    public CheckoutSummaryResponse getSummary(CheckoutSummaryRequest request) {

        String promotionId = request.getPromotionId() ;
        BigDecimal subTotal = calculateCartTotal(request);

        BigDecimal discountAmount = promotionService.calculateDiscountAmount(promotionId,subTotal);

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
