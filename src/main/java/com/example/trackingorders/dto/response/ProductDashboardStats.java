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
public class ProductDashboardStats implements Serializable {
    private BigDecimal totalInventoryValue;
    private Integer totalProductCount;
    private Integer lowStockCount;

    public ProductDashboardStats(BigDecimal totalInventoryValue, Long totalProductCount, Long lowStockCount) {
        this.totalInventoryValue = totalInventoryValue;
        this.totalProductCount = totalProductCount == null ? 0 : totalProductCount.intValue();
        this.lowStockCount = lowStockCount == null ? 0 : lowStockCount.intValue();
    }
}
