package com.example.trackingorders.entity;

import com.example.trackingorders.common.OriginType;
import com.example.trackingorders.common.StatusReturnEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "returns")
public class Returns extends BaseEntity implements Serializable {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id ;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders ;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusReturnEnum status ;

    @Column(name = "reason")
    private String reason ;

    @Column(name = "received_at")
    private LocalDateTime receivedAt ;

    @Column(name = "origin_type")
    @Enumerated(EnumType.STRING)
    private OriginType originType ;

}
