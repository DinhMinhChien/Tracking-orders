package com.example.trackingorders.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReturnSummaryResponse implements Serializable {
    private Integer activeReturns;
    private Integer awaitingInspection ;
    private Integer totalRefunds ;
}
