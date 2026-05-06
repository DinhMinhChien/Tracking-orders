package com.example.trackingorders.mapper;

import com.example.trackingorders.dto.response.CarriersResponse;
import com.example.trackingorders.entity.Carriers;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarriersMapper {
    CarriersResponse toResponse(Carriers carriers) ;
}
