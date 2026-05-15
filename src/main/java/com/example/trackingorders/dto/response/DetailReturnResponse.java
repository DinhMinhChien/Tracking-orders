package com.example.trackingorders.dto.response;

import com.example.trackingorders.common.OriginType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetailReturnResponse implements Serializable {
    private OrderDetailResponse orders ;
    private String reason ;
    private LocalDateTime receivedAt ;
    private OriginType originType ;
}
