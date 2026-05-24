package com.example.trackingorders.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "List orderIds is not empty")
    private List<@NotBlank(message = "orderId is not null") String> orderIds;
}
