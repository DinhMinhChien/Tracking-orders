package com.example.trackingorders.service.implement;

import com.example.trackingorders.common.StatusOrderEnum;
import com.example.trackingorders.common.StatusReturnEnum;
import com.example.trackingorders.dto.response.TrackingLogsResponse;
import com.example.trackingorders.entity.TrackingLogs;
import com.example.trackingorders.mapper.TrackingLogMapper;
import com.example.trackingorders.repository.TrackingLogsRepository;
import com.example.trackingorders.service.TrackingLogsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TrackingLogsServiceImplement implements TrackingLogsService {
    private final TrackingLogsRepository trackingLogsRepository ;
    private final TrackingLogMapper trackingLogMapper ;
    @Override
    public List<TrackingLogsResponse> getTrackLog(String orderId) {
        List<TrackingLogs> trackingLogs = trackingLogsRepository.findAllByOrderIdOrderByUpdatedAtDesc(orderId) ;
        List<TrackingLogsResponse> responses = trackingLogMapper.toResponses(trackingLogs);
        return responses ;
    }

    @Override
    public void createLog(String orderId, String fromStatus, String toStatus, String note, String location) {
        TrackingLogs log = new TrackingLogs() ;
        log.setOrderId(orderId);
        log.setFromStatus(fromStatus);
        log.setToStatus(toStatus);
        log.setNote(note);
        log.setLocation(location);
        trackingLogsRepository.save(log) ;
    }

    @Override
    public void createLogs(List<String> orderIds, String fromStatus, String toStatus, String note, String location) {
        List<TrackingLogs> logs = orderIds.stream().map(orderId -> {
            TrackingLogs log = new TrackingLogs() ;
            log.setOrderId(orderId);
            log.setFromStatus(fromStatus);
            log.setToStatus(toStatus);
            log.setNote(note);
            log.setLocation(location);
            return log ;
        }).toList();
        trackingLogsRepository.saveAll(logs) ;
    }

}
