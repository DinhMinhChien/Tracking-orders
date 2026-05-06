package com.example.trackingorders.dto.response;

import com.example.trackingorders.common.OriginType;
import com.example.trackingorders.common.StatusReturnEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnsResponse implements Serializable {
    private String id ;
    private String customer ;
    private String reason ;
    private OriginType originType ;
    private StatusReturnEnum status ;
}
