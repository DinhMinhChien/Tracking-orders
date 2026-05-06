package com.example.trackingorders.service.implement;

import com.example.trackingorders.dto.response.AddressResponse;
import com.example.trackingorders.entity.Addresses;
import com.example.trackingorders.mapper.AddressesMapper;
import com.example.trackingorders.repository.AddressesRepository;
import com.example.trackingorders.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersServiceImplement implements UsersService {
    private final AddressesRepository addressesRepository ;
    private final AddressesMapper addressesMapper ;
    @Override
    public List<AddressResponse> getAddresses() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName() ;
        List<Addresses> addresses = addressesRepository.findByUserName(username) ;
        List<AddressResponse> response = addressesMapper.toResponse(addresses) ;
        return response;
    }
}
