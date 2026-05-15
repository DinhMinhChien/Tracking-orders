package com.example.trackingorders.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnSummaryResponse implements Serializable {
    private Long activeReturns;
    private Long awaitingInspection ;
    private BigDecimal totalRefunds ;
}
