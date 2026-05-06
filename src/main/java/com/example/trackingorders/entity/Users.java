package com.example.trackingorders.entity;

import com.example.trackingorders.common.RoleEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Users extends BaseEntity implements Serializable {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id ;

    @Column(name = "username")
    private String username ;

    @Column(name = "password")
    private String password ;

    @Column(name = "full_name")
    private String fullName ;

    @Column(name = "phone")
    private String phone ;

    @Column(name = "email")
    private String email ;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleEnum role ;

    @OneToOne
    @JoinColumn(name = "carrier_id")
    private Carriers carrier ;

    @OneToMany(mappedBy = "users",fetch = FetchType.LAZY)
    List<Addresses> addresses ;
}
