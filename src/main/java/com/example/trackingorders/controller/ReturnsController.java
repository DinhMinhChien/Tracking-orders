package com.example.trackingorders.controller;

import com.alibaba.excel.EasyExcel;
import com.example.trackingorders.common.BaseResponse;
import com.example.trackingorders.common.StatusReturnEnum;
import com.example.trackingorders.dto.ReturnOrderExportDTO;
import com.example.trackingorders.dto.request.ReturnsRequest;
import com.example.trackingorders.dto.response.DetailReturnResponse;
import com.example.trackingorders.dto.response.ReturnSummaryResponse;
import com.example.trackingorders.dto.response.ReturnsResponse;
import com.example.trackingorders.service.ReturnsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/returns")
public class ReturnsController {

    private final ReturnsService returnsService;

    //http://localhost:8001/api/v1/returns/summary
    @GetMapping("/summary")
    public ResponseEntity<BaseResponse<ReturnSummaryResponse>> getSummary() {
        ReturnSummaryResponse response = returnsService.getSummary();
        return ResponseEntity.ok(BaseResponse.ofSuccess(response, "success"));
    }

    //http://localhost:8001/api/v1/returns/filter?status =?pageNumber=?pageSize=
    @GetMapping("/filter")
    public ResponseEntity<BaseResponse<List<ReturnsResponse>>> getAll(@RequestParam(required = false) StatusReturnEnum status,
                                                                      @RequestParam(required = false) Integer pageNumber,
                                                                      @RequestParam(required = false) Integer pageSize) {
        Page<ReturnsResponse> responses = returnsService.getAll(status, pageNumber, pageSize);
        return ResponseEntity.ok(BaseResponse.ofSuccess(responses));
    }

    //http://localhost:8001/api/v1/returns
    @PostMapping
    public ResponseEntity<BaseResponse<ReturnsResponse>> create(@RequestBody ReturnsRequest request) {
        ReturnsResponse response = returnsService.create(request);
        return ResponseEntity.ok(BaseResponse.ofSuccess(response, "Tạo thành công đơn hoàn trả"));
    }

    //http://localhost:8001/api/v1/returns/{returnId}
    @GetMapping("/{returnId}")
    public ResponseEntity<BaseResponse<DetailReturnResponse>> getDetail(@PathVariable String returnId) {
        DetailReturnResponse response = returnsService.getDetail(returnId);
        return ResponseEntity.ok(BaseResponse.ofSuccess(response, "success"));
    }

    //http://localhost:8001/api/v1/returns/export
    @GetMapping("/export")
    public void exportReturn(@RequestParam(required = false) StatusReturnEnum status,
                             HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");

        String fileName = URLEncoder.encode("Return_Report", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");

        List<ReturnOrderExportDTO> exportData = returnsService.getDataForExport(status);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        EasyExcel.write(outputStream, ReturnOrderExportDTO.class)
                .sheet("Returns")
                .doWrite(exportData);

        byte[] excelBytes = outputStream.toByteArray();
        response.setContentLength(excelBytes.length);
        response.getOutputStream().write(excelBytes);
        response.flushBuffer();
    }

    //http://localhost:8001/api/v1/returns/admin/{returnId}/confirm
    @PostMapping("/admin/{returnId}/confirm")
    public ResponseEntity<BaseResponse<String>> confirm(@PathVariable String returnId) {
        returnsService.confirm(returnId) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Confirm success")) ;
    }

    @PostMapping("/admin/{returnId}/reject")
    public ResponseEntity<BaseResponse<String>> reject(@PathVariable String returnId) {
        returnsService.reject(returnId) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Reject return success")) ;
    }

    @PostMapping("/admin/{returnId}/warehouse-received")
    public ResponseEntity<BaseResponse<String>> warehouseReceived(@PathVariable String returnId) {
        returnsService.markWarehouseReceived(returnId) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Warehouse received success")) ;
    }

    @PostMapping("/admin/{returnId}/restock")
    public ResponseEntity<BaseResponse<String>> restock(@PathVariable String returnId) {
        returnsService.restock(returnId) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Restock success")) ;
    }

    @PostMapping("/admin/{returnId}/refund")
    public ResponseEntity<BaseResponse<String>> refund(@PathVariable String returnId) {
        returnsService.refund(returnId) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Refund success")) ;
    }

    @PostMapping("/admin/{returnId}/fail")
    public ResponseEntity<BaseResponse<String>> fail(@PathVariable String returnId) {
        returnsService.fail(returnId) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Return failed")) ;
    }

}
