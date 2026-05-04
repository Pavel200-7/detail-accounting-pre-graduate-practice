package com.example.demo.presentation.dto.response;

import com.example.demo.domain.enums.MovementType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class MovementResponseDto {
    private UUID id;
    private MovementType type;
    private Integer quantity;
    private Integer quantityBefore;
    private Integer quantityAfter;
    private String documentType;
    private LocalDateTime performedAt;
    private String comment;
    private String performedByFullName;
    private UUID performedById;
    private PartInfo part;

    @Data
    @Builder
    public static class PartInfo {
        private UUID id;
        private String name;
        private String sku;
    }
}