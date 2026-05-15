package com.example.trackingorders.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BulkConfirmRequest implements Serializable {
    @NotBlank(message = "List orderIds is not null")
    private List<String> orderIds;
}
