package com.example.trackingorders.controller;

import com.example.trackingorders.common.BaseResponse;
import com.example.trackingorders.dto.response.AddressResponse;
import com.example.trackingorders.entity.Addresses;
import com.example.trackingorders.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //http://localhost:8001/api/v1/users/addresses
    @PostMapping("/addresses")
    public ResponseEntity<BaseResponse<String>> insertAddress(@RequestBody String newAddress) {
        usersService.insertAddress(newAddress) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Insert address success")) ;
    }
}
