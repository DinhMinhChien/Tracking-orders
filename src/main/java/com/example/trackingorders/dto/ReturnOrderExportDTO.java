package com.example.trackingorders.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReturnOrderExportDTO {
    @ExcelProperty("RETURN ID")
    private String returnId;

    @ExcelProperty("CUSTOMER / ORDER")
    private String customerInfo;

    @ExcelProperty("REASON")
    private String reason;

    @ExcelProperty("ORIGIN TYPE")
    private String originType;

    @ExcelProperty("STATUS")
    private String status;
}
