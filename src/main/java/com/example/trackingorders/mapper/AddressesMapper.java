package com.example.trackingorders.mapper;

import com.example.trackingorders.dto.response.AddressResponse;
import com.example.trackingorders.entity.Addresses;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressesMapper {
    List<AddressResponse> toResponse(List<Addresses> addresses) ;
}
