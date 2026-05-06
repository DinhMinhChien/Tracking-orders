package com.example.trackingorders.mapper;

import com.example.trackingorders.dto.response.TrackingLogsResponse;
import com.example.trackingorders.entity.TrackingLogs;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrackingLogMapper {
    @Mapping(source = "toStatus",target = "status")
    @Mapping(source = "createdBy",target = "actor")
    TrackingLogsResponse toResponse(TrackingLogs trackingLogs);

    List<TrackingLogsResponse> toResponses(List<TrackingLogs> trackingLogs) ;
}
