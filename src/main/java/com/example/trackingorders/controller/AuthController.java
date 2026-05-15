package com.example.trackingorders.controller;

import com.example.trackingorders.common.BaseResponse;
import com.example.trackingorders.dto.request.AuthRegisterRequest;
import com.example.trackingorders.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService ;
    private final MessageSource messageSource ;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<String>> register(@RequestBody @Valid AuthRegisterRequest request) {
        authService.register(request) ;

        String message = messageSource.getMessage(
                "Register.success",
                null,
                LocaleContextHolder.getLocale()
        );
        return ResponseEntity.ok(BaseResponse.ofSuccess("Register.success")) ;
    }

}
