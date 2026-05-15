package com.example.trackingorders.dto.request;

import com.example.trackingorders.common.RoleEnum;
import com.example.trackingorders.entity.Carriers;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRegisterRequest implements Serializable {
    @NotBlank(message = "Field username is not null")
    private String username ;

    @NotBlank(message = "Field password is not null")
    private String password ;

    @NotBlank(message = "Field fullName is not null")
    private String fullName ;

    @NotBlank(message = "Field phone is not null")
    private String phone ;

    @NotBlank(message = "Field email is not null")
    private String email ;

    @NotBlank(message = "Field role is not null")
    private RoleEnum role ;

    private String carrierId ;

    @NotBlank(message = "Field address is not null")
    private String address ;

}
