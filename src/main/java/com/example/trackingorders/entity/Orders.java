package com.example.trackingorders.entity;

import com.example.trackingorders.common.StatusOrderEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Orders extends BaseEntity implements Serializable {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id ;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethods paymentMethod ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotions promotions ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrier_id")
    private Carriers carriers ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_id")
    private Users shipper ;

    @Column(name = "total_price")
    private BigDecimal totalPrice ;

    @Column(name = "shipping_fee")
    private BigDecimal shippingFee ;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusOrderEnum status ;

    @Column(name = "shipping_address")
    private String shippingAddress ;

    @OneToMany(mappedBy = "orders")
    private List<OrderItems> orderItems ;
}
