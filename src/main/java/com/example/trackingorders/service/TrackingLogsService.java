package com.example.trackingorders.service;

import com.example.trackingorders.dto.response.TrackingLogsResponse;

import java.util.List;

public interface TrackingLogsService {
    List<TrackingLogsResponse> getTrackLog(String orderId) ;
}
