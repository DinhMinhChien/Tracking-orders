package com.example.trackingorders.service.implement;

import com.example.trackingorders.dto.response.TrackingLogsResponse;
import com.example.trackingorders.entity.TrackingLogs;
import com.example.trackingorders.mapper.TrackingLogMapper;
import com.example.trackingorders.repository.TrackingLogsRepository;
import com.example.trackingorders.service.TrackingLogsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackingLogsServiceImplement implements TrackingLogsService {
    private final TrackingLogsRepository trackingLogsRepository ;
    private final TrackingLogMapper trackingLogMapper ;
    @Override
    public List<TrackingLogsResponse> getTrackLog(String orderId) {
        List<TrackingLogs> trackingLogs = trackingLogsRepository.findAllByOrderIdOrderByUpdatedAtDesc(orderId) ;
        List<TrackingLogsResponse> responses = trackingLogMapper.toResponses(trackingLogs);
        return responses ;
    }
}
