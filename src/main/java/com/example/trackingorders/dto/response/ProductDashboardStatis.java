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
public class ProductDashboardStatis implements Serializable {
    private BigDecimal totalInventoryValue;
    private Integer totalProductCount;
    private Integer lowStockCount;
}
