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
@Schema(description = "DTO остатка на складе")
public class StockResponse {
    private UUID partId;
    private String partName;
    private String unit;
    @Schema(description = "Текущее количество", example = "8")
    private Integer quantity;
    @Schema(description = "Минимальный остаток", example = "2")
    private Integer minStock;
    @Schema(description = "Флаг критического остатка", example = "false")
    private Boolean isLowStock;
}
