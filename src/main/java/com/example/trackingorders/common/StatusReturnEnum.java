package com.example.trackingorders.common;

public enum StatusReturnEnum {
    PENDING_APPROVAL,  // Chờ duyệt yêu cầu
    PENDING_ACTION,    // Chờ xử lý tình huống (giao lỗi)
    IN_TRANSIT,        // Đang vận chuyển về
    WAREHOUSE_RECEIVED, // Kho đã nhận
    RESTOCKED,         // Đã nhập lại kho (thành công)
    REFUNDED,          // Đã hoàn tiền
    REJECTED,          // Đã từ chối
    DAMAGED
}
