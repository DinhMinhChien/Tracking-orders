package com.example.trackingorders.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest implements Serializable {

    @NotBlank(message = "Field name is not null")
    private String name ;

    @NotNull(message = "Field price not null")
    @Positive(message = "Price is higher than zero")
    private BigDecimal price ;

    @NotBlank(message = "Field description is not null")
    @Size(min = 5 , max = 500,message = "Description must be between 5 and 500 characters")
    private String description ;

    private Boolean isFeature ;

    @NotBlank(message = "Field sku is not null")
    private String sku ;

    @NotBlank(message = "Field imgUrl is not null")
    private String imgUrl ;

    @NotBlank(message = "Field quantity is not null")
    private Integer quantity ;
}
