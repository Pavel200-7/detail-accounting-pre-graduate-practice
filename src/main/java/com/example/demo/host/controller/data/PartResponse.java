package com.example.demo.host.controller.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO детали (материальной ценности)")
public class PartResponse {
    @Schema(description = "Уникальный идентификатор", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID id;
    @Schema(description = "Наименование детали", example = "Системный блок HP ProDesk")
    private String name;
    @Schema(description = "Артикул / SKU", example = "PC-HP-001")
    private String sku;
    @Schema(description = "Единица измерения", example = "PCS", allowableValues = {"PCS", "KG", "M", "PACK", "SET", "L"})
    private String unit;
    @Schema(description = "Категория детали", example = "EQUIPMENT")
    private String category;
    @Schema(description = "Минимальный остаток для оповещения", example = "2")
    private Integer minStock;
    @Schema(description = "Оценочная стоимость", example = "25000.00")
    private BigDecimal price;
}
