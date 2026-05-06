package com.example.trackingorders.mapper;

import com.example.trackingorders.dto.response.PromotionsResponse;
import com.example.trackingorders.entity.Promotions;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PromotionsMapper {
    List<PromotionsResponse> toResponse(List<Promotions> promotions) ;
    PromotionsResponse toResponse(Promotions promotions) ;
}
