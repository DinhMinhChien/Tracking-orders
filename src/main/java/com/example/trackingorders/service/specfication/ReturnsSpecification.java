package com.example.trackingorders.service.specfication;

import com.example.trackingorders.common.StatusReturnEnum;
import com.example.trackingorders.entity.Returns;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

public class ReturnsSpecification {
    public static Specification<Returns> likeStatus(StatusReturnEnum status) {
        return new Specification<Returns>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Returns> root, @Nullable CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (status == null ) {
                    return criteriaBuilder.conjunction() ;
                }
                return criteriaBuilder.like(root.get("status"),"%" + status + "%") ;

            }
        };
    }
}
