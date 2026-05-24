package com.example.trackingorders.dto.response;

import com.example.trackingorders.common.StatusOrderEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse implements Serializable {
    private String id ;
    private UsersResponse users ;
    private PaymentMethodsResponse paymentMethod ;
    private PromotionsResponse promotions ;
    private CarriersResponse carriers ;
    private UsersResponse shipper ;
    private BigDecimal totalPrice ;
    private BigDecimal shippingFee ;
    private StatusOrderEnum status ;
    private String shippingAddress ;
    private List<OrderItemResponse> orderItems ;
    private LocalDateTime createdAt ;
}
