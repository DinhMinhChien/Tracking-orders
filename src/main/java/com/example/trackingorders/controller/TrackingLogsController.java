package com.example.trackingorders.controller;

import com.example.trackingorders.common.BaseResponse;
import com.example.trackingorders.dto.response.TrackingLogsResponse;
import com.example.trackingorders.repository.TrackingLogsRepository;
import com.example.trackingorders.service.TrackingLogsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tracking-logs")
public class TrackingLogsController {
    private final TrackingLogsService trackingLogsService ;

    @GetMapping("/{orderId}")
    public ResponseEntity<BaseResponse<List<TrackingLogsResponse>>> getTrackLog(@PathVariable String orderId) {
        List<TrackingLogsResponse> trackingLogsResponses = trackingLogsService.getTrackLog(orderId) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess(trackingLogsResponses,"Lấy thành công ghi chú đơn hàng")) ;
    }

}
