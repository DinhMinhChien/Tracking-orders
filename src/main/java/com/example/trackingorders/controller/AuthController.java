package com.example.trackingorders.controller;

import com.example.trackingorders.common.BaseResponse;
import com.example.trackingorders.dto.request.AuthRegisterRequest;
import com.example.trackingorders.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService ;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<String>> register(@RequestBody AuthRegisterRequest request) {
        authService.register(request) ;
        return ResponseEntity.ok(BaseResponse.ofSuccess("Đăng ký tài khoản thành công")) ;
    }

}
