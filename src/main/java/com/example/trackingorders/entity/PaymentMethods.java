package com.example.trackingorders.entity;

import com.example.trackingorders.common.StatusProductsEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payment_methods")
public class PaymentMethods extends BaseEntity implements Serializable {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id ;

    @Column(name = "type")
    private String type ;

    @Column(name = "name")
    private String name ;

    @Column(name = "is_online")
    private Boolean isOnline ;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusProductsEnum status ;
}
