package com.example.trackingorders.dto.response;

import com.example.trackingorders.common.RoleEnum;
import com.example.trackingorders.entity.Addresses;
import com.example.trackingorders.entity.Carriers;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsersResponse implements Serializable {
    private String id ;
    private String fullName ;
    private String phone ;
    private String email ;



}
