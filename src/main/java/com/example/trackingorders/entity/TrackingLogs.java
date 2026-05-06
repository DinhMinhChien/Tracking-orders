package com.example.trackingorders.entity;

import com.example.trackingorders.common.StatusOrderEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tracking_logs")
public class TrackingLogs extends BaseEntity implements Serializable {
    @Id
    @UuidGenerator
    @Column(name = "id")
    private String id ;

    @Column(name = "order_id")
    private String orderId ;

    @Column(name = "from_status")
    @Enumerated(EnumType.STRING)
    private StatusOrderEnum fromStatus ;

    @Column(name = "to_status")
    @Enumerated(EnumType.STRING)
    private StatusOrderEnum toStatus ;

    @Column(name = "note")
    private String note ;

    @Column(name = "location")
    private String  location ;

}
