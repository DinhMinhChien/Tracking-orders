package com.example.trackingorders.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;

@Entity
@Setter
@Getter
@Table(name = "user_promotions")
public class UserPromotions extends BaseEntity implements Serializable {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id ;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users ;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotions promotions ;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders ;
}
