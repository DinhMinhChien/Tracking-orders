package com.example.trackingorders.controller;

import com.alibaba.excel.EasyExcel;
import com.example.trackingorders.common.BaseResponse;
import com.example.trackingorders.common.StatusProductsEnum;
import com.example.trackingorders.dto.ProductsExportDTO;
import com.example.trackingorders.dto.ReturnOrderExportDTO;
import com.example.trackingorders.dto.request.ProductCreateRequest;
import com.example.trackingorders.dto.request.ProductsUpdateRequest;
import com.example.trackingorders.dto.response.ProductCreateResponse;
import com.example.trackingorders.dto.response.ProductDashboardStatis;
import com.example.trackingorders.dto.response.ProductsResponse;
import com.example.trackingorders.service.ProductsService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
@Validated
public class ProductsController {
    private final ProductsService productsService ;
    //http://localhost:8001/api/v1/products
    @PostMapping
    public ResponseEntity<BaseResponse<ProductCreateResponse>> create(@RequestBody @Valid ProductCreateRequest request) {
        ProductCreateResponse response = productsService.create(request) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess(response,"CreateProductSuccess.message"));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<ProductsResponse>>> getAll(@RequestParam(required = false) StatusProductsEnum status,
                                                                       @RequestParam(required = false) String keyword,
                                                                       @RequestParam(required = false) Boolean isFeature,
                                                                       @RequestParam(required = false,defaultValue = "1") int pageNumber,
                                                                       @RequestParam(required = false,defaultValue = "4") int pageSize) {
        Page<ProductsResponse> responses = productsService.getAll(status,keyword,isFeature,pageNumber,pageSize) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess(responses)) ;
    }

    @GetMapping("/statistics")
    public ResponseEntity<BaseResponse<ProductDashboardStatis>> getStats() {
        ProductDashboardStatis responses = productsService.getStatis();
        return ResponseEntity.ok(BaseResponse.ofSuccess(responses,"Lấy thành công số liệu thống kê")) ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductsResponse>> getDetail(@PathVariable String id) {
        ProductsResponse response = productsService.getDetail(id) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess(response,"Lấy chi tiết sản phẩm thành công ")) ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ProductsResponse>> update(@PathVariable String id, @RequestBody ProductsUpdateRequest request) {
        ProductsResponse response = productsService.update(id,request) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess(response,"Cập nhật sản phẩm thành công")) ;
    }

    @GetMapping("/export")
    public void exportProducts(@RequestParam(required = false) StatusProductsEnum status,
                               @RequestParam(required = false) String keyword,
                               HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");

        String fileName = URLEncoder.encode("Products_Report", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");

        List<ProductsExportDTO> exportData = productsService.getDataForExport(status,keyword);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        EasyExcel.write(outputStream, ReturnOrderExportDTO.class)
                .sheet("Products")
                .doWrite(exportData);

        byte[] excelBytes = outputStream.toByteArray();
        response.setContentLength(excelBytes.length);
        response.getOutputStream().write(excelBytes);
        response.flushBuffer();
    }

}
