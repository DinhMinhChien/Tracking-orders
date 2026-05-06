package com.example.trackingorders.entity;

import com.example.trackingorders.common.StatusProductsEnum;
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
@Table(name = "products")
public class Products extends BaseEntity implements Serializable {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id ;

    @Column(name = "name")
    private String name ;

    @Column(name = "price")
    private BigDecimal price ;

    @Column(name = "description")
    private String description ;

    @Column(name = "is_feature")
    private Boolean isFeature ;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusProductsEnum status ;

    @Column(name = "sku")
    private String sku ;

    @Column(name = "img_url")
    private String imgUrl ;

}
