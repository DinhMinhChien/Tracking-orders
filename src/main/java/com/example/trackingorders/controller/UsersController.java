package com.example.trackingorders.controller;

import com.example.trackingorders.common.BaseResponse;
import com.example.trackingorders.dto.response.AddressResponse;
import com.example.trackingorders.entity.Addresses;
import com.example.trackingorders.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService ;

    //http://localhost:8001/api/v1/users/addresses
    @GetMapping("/addresses")
    public ResponseEntity<BaseResponse<List<AddressResponse>>> getAddresses() {
        List<AddressResponse> addresses = usersService.getAddresses() ;
        return ResponseEntity.ok(BaseResponse.ofSuccess(addresses,"success")) ;
    }
}
