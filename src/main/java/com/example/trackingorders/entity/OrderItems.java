package com.example.trackingorders.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
public class OrderItems extends BaseEntity implements Serializable {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id ;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders ;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products products ;

    @Column(name = "quantity")
    private Integer quantity ;

    @Column(name = "price")
    private BigDecimal price ;
}
