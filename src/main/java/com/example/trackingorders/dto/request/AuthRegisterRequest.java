package com.example.trackingorders.dto.request;

import com.example.trackingorders.common.RoleEnum;
import com.example.trackingorders.entity.Carriers;
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
    private String username ;
    private String password ;
    private String fullName ;
    private String phone ;
    private String email ;
    private RoleEnum role ;
    private String carrierId ;
    private String address ;

}
