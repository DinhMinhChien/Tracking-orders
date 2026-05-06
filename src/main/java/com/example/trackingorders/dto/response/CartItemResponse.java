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
public class CartItemResponse implements Serializable {
    private String id ;
    private Integer quantity ;
    private ProductsResponse product ;
    private StatusProductsEnum status ;
}
