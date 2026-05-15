package com.example.trackingorders.service;

import com.example.trackingorders.common.StatusOrderEnum;
import com.example.trackingorders.dto.response.TrackingLogsResponse;

import java.util.List;

public interface TrackingLogsService {
    List<TrackingLogsResponse> getTrackLog(String orderId) ;

    void createLog(String orderId , String fromStatus, String toStatus, String note, String location) ;

    void createLogs(List<String> orderIds, String fromStatus, String toStatus, String note, String location) ;
}
