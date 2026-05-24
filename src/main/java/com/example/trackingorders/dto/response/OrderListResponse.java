package com.example.trackingorders.dto.response;

import com.example.trackingorders.common.StatusOrderEnum;
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
public class OrderListResponse implements Serializable {
    private String id ;
    private String customerName ;
    private LocalDateTime createdAt ;
    private String paymentMethod ;
    private BigDecimal totalPrice ;
    private StatusOrderEnum status ;
    private String carrierName ;
}
