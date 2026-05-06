package com.example.trackingorders.dto.response;

import com.example.trackingorders.common.StatusProductsEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

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
    private StatusProductsEnum status ;
}
