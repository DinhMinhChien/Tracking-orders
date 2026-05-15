package com.example.trackingorders.dto.request;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Field productIds is not null")
    private List<String> productIds ;

    @NotBlank(message = "Field quantities is not null")
    private List<Integer> quantities ;

    @NotBlank(message = "Field promotionId is not null")
    private String promotionId ;

    @NotBlank(message = "Field addressId is not null")
    private String addressId ;

    @NotBlank(message = "Field isFromCart is not null")
    private Boolean isFromCart ;
}
