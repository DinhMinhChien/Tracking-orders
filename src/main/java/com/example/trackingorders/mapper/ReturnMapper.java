package com.example.trackingorders.mapper;

import com.example.trackingorders.dto.response.ReturnsResponse;
import com.example.trackingorders.entity.Returns;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReturnMapper {
    @Mapping(target = "customer", source = "orders.users.fullName")
    ReturnsResponse toResponse(Returns entity);

    List<ReturnsResponse> toResponses(List<Returns> entities);
}
