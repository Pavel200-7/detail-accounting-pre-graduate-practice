package com.example.demo.presentation.dto.response;

import com.example.demo.domain.enums.PartCategory;
import com.example.demo.domain.enums.Unit;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PartWithStockResponseDto {
    private UUID id;
    private String name;
    private String sku;
    private Unit unit;
    private PartCategory category;
    private BigDecimal price;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private StockInfo stock;

    @Data
    @Builder
    public static class StockInfo {
        private Integer quantity;
        private Integer reservedQuantity;
        private LocalDateTime lastUpdated;
    }
}