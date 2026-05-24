package com.example.trackingorders.service;

import com.example.trackingorders.common.StatusReturnEnum;
import com.example.trackingorders.dto.ReturnOrderExportDTO;
import com.example.trackingorders.dto.request.ReturnsRequest;
import com.example.trackingorders.dto.response.DetailReturnResponse;
import com.example.trackingorders.dto.response.ReturnSummaryResponse;
import com.example.trackingorders.dto.response.ReturnsResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ReturnsService {
    ReturnSummaryResponse getSummary() ;
    Page<ReturnsResponse> getAll(StatusReturnEnum status, Integer pageNumber, Integer pageSize) ;
    ReturnsResponse create(ReturnsRequest request) ;
    DetailReturnResponse getDetail(String returnId) ;
    List<ReturnOrderExportDTO> getDataForExport(StatusReturnEnum status) ;
    void confirm(String returnId) ;
    void reject(String returnId) ;
    void markWarehouseReceived(String returnId) ;
    void restock(String returnId) ;
    void refund(String returnId) ;
    void fail(String returnId) ;

}
