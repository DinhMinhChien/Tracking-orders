package com.example.trackingorders.dto.response;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromotionsResponse implements Serializable {
    private String id ;
    private String name ;
    private String code ;
    private String discountType ;
    private Integer discountValue ;
    private BigDecimal minOrderValue;
    private LocalDateTime startDate ;
    private LocalDateTime endDate ;
    private Integer usagesLimit ;
}
