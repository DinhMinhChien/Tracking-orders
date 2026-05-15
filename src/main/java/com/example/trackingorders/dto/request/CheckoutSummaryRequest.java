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
public class CheckoutSummaryRequest implements Serializable {

    @NotBlank(message = "Field cartItemIds not null")
    private List<String> productIds;

    @NotBlank(message = "Field quantity not null")
    private List<Integer> quantities ;

    private String promotionId ;
}
