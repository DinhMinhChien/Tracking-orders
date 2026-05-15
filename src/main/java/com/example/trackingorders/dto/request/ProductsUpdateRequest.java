package com.example.trackingorders.dto.request;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Field name is not null")
    private String name ;

    @NotBlank(message ="Field sku is not null")
    private String sku ;

    @NotBlank(message = "Field price is not null")
    private BigDecimal price ;

    @NotBlank(message = "Field quantity is not null")
    private Integer quantity ;

    @NotBlank(message = "Field description is not null")
    private String description ;
}
