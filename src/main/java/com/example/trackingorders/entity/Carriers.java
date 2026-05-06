package com.example.trackingorders.entity;

import com.example.trackingorders.common.StatusEnum;
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
@Table(name = "carriers")
public class Carriers extends BaseEntity implements Serializable {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id ;

    @Column(name = "name")
    private String name ;

    @Column(name = "logo_url")
    private String logoUrl ;

    @Column(name = "description")
    private String description ;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusEnum status ;
}
