package com.example.trackingorders.exception;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String message ;
    private List<String> systemMessage ;
    private int code ;
}
