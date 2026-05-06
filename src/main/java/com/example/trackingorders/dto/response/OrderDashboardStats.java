package com.example.trackingorders.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDashboardStats implements Serializable {
    private BigDecimal mtdRevenue;
    private long totalOrders;
    private long pendingOrders;
    private long shippingOrders;
    private long failedOrders;
}
