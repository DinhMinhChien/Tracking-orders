package com.example.trackingorders.dto.request;

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
public class ProductsUpdateRequest implements Serializable {
    private String name ;
    private String sku ;
    private String category ;
    private BigDecimal price ;
    private Integer quantity ;
    private String description ;
}
