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
public class ProductsResponse implements Serializable {
    private String id ;
    private String name ;
    private BigDecimal price ;
    private String sku ;
    private String imgUrl ;
    private int stock ;
}
