package com.example.trackingorders.dto.response;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackingLogsResponse implements Serializable {
    private String id ;
    private String orderId ;
    private String status ;
    private String note ;
    private String location ;
    private String actor ;
}
