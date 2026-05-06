package com.example.trackingorders.mapper;

import com.example.trackingorders.dto.response.UsersResponse;
import com.example.trackingorders.entity.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    UsersResponse toResponse(Users user) ;
}
