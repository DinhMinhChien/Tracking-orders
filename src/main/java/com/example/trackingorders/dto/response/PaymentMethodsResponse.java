package com.example.trackingorders.dto.response;

import com.example.trackingorders.common.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodsResponse implements Serializable {
    private String id ;
    private String type ;
    private String name ;
    private Boolean isOnline ;
    private StatusEnum status ;
}
