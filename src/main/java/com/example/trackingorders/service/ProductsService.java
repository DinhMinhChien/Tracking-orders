package com.example.trackingorders.service;

import com.example.trackingorders.common.StatusProductsEnum;
import com.example.trackingorders.dto.ProductsExportDTO;
import com.example.trackingorders.dto.request.ProductCreateRequest;
import com.example.trackingorders.dto.request.ProductsUpdateRequest;
import com.example.trackingorders.dto.response.ProductCreateResponse;
import com.example.trackingorders.dto.response.ProductDashboardStats;
import com.example.trackingorders.dto.response.ProductsResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductsService {
    ProductCreateResponse create(ProductCreateRequest request) ;
    Page<ProductsResponse> getAll(StatusProductsEnum status,String keyword,Boolean isFeature,int pageNumber,int pageSize) ;

    ProductDashboardStats getStats() ;
    ProductsResponse getDetail(String id) ;
    ProductsResponse update(String id,ProductsUpdateRequest request) ;
    List<ProductsExportDTO> getDataForExport(StatusProductsEnum status,String keyword) ;
}
