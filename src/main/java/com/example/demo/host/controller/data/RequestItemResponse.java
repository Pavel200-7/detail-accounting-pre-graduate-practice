package com.example.demo.host.controller.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO позиции в заявке")
public class RequestItemResponse {
    private UUID partId;
    private String partName;
    private Integer quantity;
    private Integer actualQuantity;
    private String unit;
}

