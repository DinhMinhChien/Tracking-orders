package com.example.trackingorders.service.implement;

import com.example.trackingorders.dto.response.PromotionsResponse;
import com.example.trackingorders.entity.Orders;
import com.example.trackingorders.entity.Promotions;
import com.example.trackingorders.entity.UserPromotions;
import com.example.trackingorders.entity.Users;
import com.example.trackingorders.exception.BusinessException;
import com.example.trackingorders.mapper.PromotionsMapper;
import com.example.trackingorders.repository.PromotionsRepository;
import com.example.trackingorders.repository.UserPromotionsRepository;
import com.example.trackingorders.repository.UsersRepository;
import com.example.trackingorders.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class PromotionServiceImplement implements PromotionService {
    private final PromotionsRepository promotionsRepository ;
    private final PromotionsMapper promotionsMapper ;
    private final UsersRepository usersRepository;
    private final UserPromotionsRepository userPromotionsRepository ;

    @Override
    @Transactional(readOnly = true)
    public List<PromotionsResponse> getAvailablePromotions(BigDecimal orderTotal) {
        LocalDateTime timeNow = LocalDateTime.now();
        List<Promotions> promotions = promotionsRepository.findAllByCondition(orderTotal, timeNow);
        List<PromotionsResponse> responses = promotionsMapper.toResponse(promotions);
        return responses ;
    }

    @Override
    public Promotions validatePromotion(String promotionId, BigDecimal subTotal) {
        if (promotionId == null || promotionId.isBlank()) {
            return null;
        }

        Promotions promotion = promotionsRepository.findById(promotionId)
                .orElseThrow(() -> new BusinessException("Promotion.fail.message"));

        LocalDateTime now = LocalDateTime.now();

        if (Boolean.TRUE.equals(promotion.getDeleted())) {
            throw new BusinessException("Promotion.expired");
        }
        if (promotion.getStartDate() != null && promotion.getStartDate().isAfter(now)) {
            throw new BusinessException("Promotion.not-started");
        }
        if (promotion.getEndDate() != null && promotion.getEndDate().isBefore(now)) {
            throw new BusinessException("Promotion.expired");
        }
        if (promotion.getUsagesLimit() == null || promotion.getUsagesLimit() <= 0) {
            throw new BusinessException("Promotion.usage-limit.message");
        }
        if (promotion.getMinOrderValue() != null && subTotal.compareTo(promotion.getMinOrderValue()) < 0) {
            throw new BusinessException("Promotion.fail.min-order-value");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName() ;
        Users user = usersRepository.findByUsername(username);

        if (userPromotionsRepository.existsByUsersAndPromotionsAndDeletedFalse(user,promotion)) {
            throw new BusinessException("Promotion-used") ;
        }

        return promotion;
    }

    @Override
    public BigDecimal calculateDiscount(Promotions promotion, BigDecimal subTotal) {
        BigDecimal discountAmount = BigDecimal.ZERO;
        String discountType = promotion.getDiscountType();

        if ("percent".equalsIgnoreCase(discountType)) {
            BigDecimal discountValue = BigDecimal.valueOf(promotion.getDiscountValue());
            discountAmount = subTotal.multiply(discountValue).divide(BigDecimal.valueOf(100));
        } else if ("fixed".equalsIgnoreCase(discountType)) {
            discountAmount = new BigDecimal(promotion.getDiscountValue());
        }

        if (discountAmount.compareTo(subTotal) > 0) {
            discountAmount = subTotal;
        }

        return discountAmount;
    }

    @Override
    public BigDecimal calculateDiscountAmount(String promotionId, BigDecimal subTotal) {
        Promotions promotion = validatePromotion(promotionId,subTotal) ;
        if (promotion == null) {
            return BigDecimal.ZERO ;
        }
        return calculateDiscount(promotion,subTotal);
    }

    @Override
    public void usePromotion(Users user , Promotions promotion, Orders order) {

        if (promotion == null) {
            return;
        }

        promotion.setUsagesLimit(promotion.getUsagesLimit() - 1);
        UserPromotions userPromotions = new UserPromotions() ;
        userPromotions.setUsers(user);
        userPromotions.setPromotions(promotion);
        userPromotions.setOrders(order);
        userPromotionsRepository.save(userPromotions) ;
        promotionsRepository.save(promotion);
    }

    @Override
    public void restorePromotion(Promotions promotion) {
        promotion.setUsagesLimit(promotion.getUsagesLimit()+1);
        UserPromotions userPromotion = userPromotionsRepository.findByPromotions(promotion) ;
        userPromotion.setDeleted(true);
        userPromotionsRepository.save(userPromotion) ;
    }
}
