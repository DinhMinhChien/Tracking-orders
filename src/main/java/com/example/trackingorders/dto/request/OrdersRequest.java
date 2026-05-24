package com.example.trackingorders.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdersRequest implements Serializable {

    @NotEmpty(message = "productIds must not be null or empty")
    private List<@NotBlank(message = "productId must not be blank") String> productIds;

    @NotEmpty(message = "quantities must not be null or empty")
    private List<@NotNull(message = "quantity must not be null")
    @Positive(message = "quantity must be greater than 0") Integer> quantities;

    @NotBlank(message = "Field promotionId is not null")
    private String promotionId ;

    @NotBlank(message = "Field addressId is not null")
    private String addressId ;

    @NotNull(message = "Field isFromCart is not null")
    private Boolean isFromCart ;
}
