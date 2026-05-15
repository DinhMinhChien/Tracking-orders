package com.example.trackingorders.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemUpdateRequest implements Serializable {

    @NotBlank(message = "Field not null")
    @Positive(message = "Quantity is higher than zero")
    private Integer quantity ;
}
