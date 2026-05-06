package com.example.trackingorders.service;

import com.example.trackingorders.dto.response.AddressResponse;
import com.example.trackingorders.entity.Addresses;

import java.util.List;

public interface UsersService {
    List<AddressResponse> getAddresses() ;
}
