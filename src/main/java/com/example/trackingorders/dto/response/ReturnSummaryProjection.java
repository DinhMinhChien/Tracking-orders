package com.example.trackingorders.dto.response;

import java.math.BigDecimal;

public interface ReturnSummaryProjection {
    Long getActiveReturns();

    Long getAwaitingInspection();

    BigDecimal getTotalRefunds();
}
