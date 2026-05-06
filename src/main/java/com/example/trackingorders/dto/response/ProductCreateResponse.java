package com.example.trackingorders.dto.response;

import com.example.trackingorders.common.StatusProductsEnum;
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
public class ProductCreateResponse implements Serializable {
    private String name ;
    private String sku ;
    private BigDecimal price ;
    private Integer quantity ;
    private StatusProductsEnum status ;
}
