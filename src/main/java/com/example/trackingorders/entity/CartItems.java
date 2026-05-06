package com.example.trackingorders.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "cart_items")
public class CartItems extends BaseEntity implements Serializable {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id ;

    @Column(name = "quantity")
    private Integer quantity ;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products products ;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Carts carts;

}
