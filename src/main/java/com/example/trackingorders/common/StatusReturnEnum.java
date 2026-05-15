package com.example.trackingorders.common;

public enum StatusReturnEnum {
    PENDING,  // Chờ duyệt yêu cầu//
    IN_TRANSIT,        // Đang vận chuyển về
    WAREHOUSE_RECEIVED, // Kho đã nhận
    RESTOCKED,         // Đã nhập lại kho (thành công)
    REFUNDED,          // Đã hoàn tiền
    REJECTED,          // Đã từ chối
    FAILED
}
