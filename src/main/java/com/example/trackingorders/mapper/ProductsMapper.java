package com.example.trackingorders.mapper;

import com.example.trackingorders.dto.ProductsExportDTO;
import com.example.trackingorders.dto.response.ProductCreateResponse;
import com.example.trackingorders.dto.response.ProductsResponse;
import com.example.trackingorders.entity.Inventory;
import com.example.trackingorders.entity.Products;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductsMapper {
    @Mapping(target = "quantity", ignore = true)
    ProductCreateResponse toResponse(Products products) ;

    @Mapping(target = "id", source = "products.id")
    @Mapping(target = "name", source = "products.name")
    @Mapping(target = "price", source = "products.price")
    @Mapping(target = "sku", source = "products.sku")
    @Mapping(target = "imgUrl", source = "products.imgUrl")
    @Mapping(target = "stock", source = "inventory.quantity")
    ProductsResponse toProductResponse(Products products, Inventory inventory) ;

    @Mapping(target = "name", source = "products.name")
    @Mapping(target = "sku", source = "products.sku")
    @Mapping(target = "price", source = "products.price")
    @Mapping(target = "status", source = "products.status")
    @Mapping(target = "quantity", source = "inventory.quantity")
    ProductsExportDTO toExportDTO(Products products,Inventory inventory) ;
}
