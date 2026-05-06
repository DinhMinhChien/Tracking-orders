package com.example.trackingorders.service.specfication;

import com.example.trackingorders.common.StatusOrderEnum;
import com.example.trackingorders.entity.Orders;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

public class OrderSpecification {
    public static Specification<Orders> likeStatus(StatusOrderEnum status) {
        return new Specification<Orders>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Orders> root, @Nullable CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (status == null) {
                    return criteriaBuilder.conjunction() ;
                }
                return criteriaBuilder.like(root.get("status"),"%" + status + "%") ;
            }
        };
    }
}
