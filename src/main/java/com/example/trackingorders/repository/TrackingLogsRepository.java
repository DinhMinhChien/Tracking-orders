package com.example.trackingorders.repository;

import com.example.trackingorders.entity.TrackingLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TrackingLogsRepository extends JpaRepository<TrackingLogs,String>, JpaSpecificationExecutor<TrackingLogs> {
    List<TrackingLogs> findAllByOrderIdOrderByUpdatedAtDesc(String orderId);
}
