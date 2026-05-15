package com.example.trackingorders.service.specfication;

import com.example.trackingorders.common.StatusReturnEnum;
import com.example.trackingorders.entity.Orders;
import com.example.trackingorders.entity.Returns;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

public class ReturnsSpecification {
    public static Specification<Returns> fetchListRelations() {
        return (root, query, criteriaBuilder) -> {
            Class<?> resultType = query.getResultType() ;
            if (resultType != Long.class && resultType != long.class) {
                Fetch<Returns, Orders> orderFetch = root.fetch("orders", JoinType.LEFT);
                orderFetch.fetch("users", JoinType.LEFT) ;
                query.distinct(true) ;
            }
            return criteriaBuilder.conjunction() ;
        };
    }
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
