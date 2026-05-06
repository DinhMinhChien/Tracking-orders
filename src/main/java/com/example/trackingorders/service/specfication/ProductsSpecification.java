package com.example.trackingorders.service.specfication;

import com.example.trackingorders.common.StatusProductsEnum;
import com.example.trackingorders.entity.Products;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

public class ProductsSpecification {
    public static Specification<Products> likeStatus(StatusProductsEnum status) {
        return new Specification<Products>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Products> root, @Nullable CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (status == null) {
                    return criteriaBuilder.conjunction() ;
                }
                return criteriaBuilder.like(root.get("status"),"%" + status + "%") ;
            }
        };
    }
    public static Specification<Products> likeSku(String sku) {
        return new Specification<Products>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Products> root, @Nullable CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if(sku == null) {
                    return criteriaBuilder.conjunction();
                }
                return criteriaBuilder.like(root.get("sku"),"%" + sku +"%") ;
            }
        };
    }
    public static Specification<Products> EqualIsFeature(Boolean isFeature) {
        return new Specification<Products>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Products> root, @Nullable CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                if (isFeature == null) {
                    return criteriaBuilder.conjunction() ;
                }
                return criteriaBuilder.equal(root.get("isFeature"),isFeature) ;
            }
        };
    }
}
