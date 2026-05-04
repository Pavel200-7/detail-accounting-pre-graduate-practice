package com.example.demo.infrastructure.mappers;

import com.example.demo.domain.entity.*;
import com.example.demo.presentation.dto.request.CreateMovementDto;
import com.example.demo.presentation.dto.request.CreatePartDto;
import com.example.demo.presentation.dto.response.MovementResponseDto;
import com.example.demo.presentation.dto.response.PartWithStockResponseDto;
import org.springframework.stereotype.Component;

@Component
public class PartMovementMapper {

    public Part toPartEntity(CreatePartDto dto) {
        Part part = new Part();
        part.setName(dto.getName());
        part.setSku(dto.getSku());
        part.setUnit(dto.getUnit());
        part.setCategory(dto.getCategory());
        part.setPrice(dto.getPrice());
        part.setDescription(dto.getDescription());
        return part;
    }

    public PartWithStockResponseDto toPartWithStockResponseDto(Part part, Stock stock) {
        return PartWithStockResponseDto.builder()
                .id(part.getId())
                .name(part.getName())
                .sku(part.getSku())
                .unit(part.getUnit())
                .category(part.getCategory())
                .price(part.getPrice())
                .description(part.getDescription())
                .createdAt(part.getCreatedAt())
                .updatedAt(part.getUpdatedAt())
                .stock(PartWithStockResponseDto.StockInfo.builder()
                        .quantity(stock != null ? stock.getQuantity() : 0)
                        .reservedQuantity(stock != null ? stock.getReservedQuantity() : 0)
                        .lastUpdated(stock != null ? stock.getLastUpdated() : null)
                        .build())
                .build();
    }

    public Movement toMovementEntity(CreateMovementDto dto, Part part, User performedBy) {
        Movement movement = new Movement();
        movement.setType(dto.getType());
        movement.setQuantity(dto.getQuantity());
        movement.setDocumentType(dto.getDocumentType());
        movement.setComment(dto.getComment());
        movement.setPart(part);
        movement.setPerformedBy(performedBy);
        return movement;
    }

    public MovementResponseDto toMovementResponseDto(Movement movement, Part part, User performedBy) {
        return MovementResponseDto.builder()
                .id(movement.getId())
                .type(movement.getType())
                .quantity(movement.getQuantity())
                .quantityBefore(movement.getQuantityBefore())
                .quantityAfter(movement.getQuantityAfter())
                .documentType(movement.getDocumentType())
                .performedAt(movement.getPerformedAt())
                .comment(movement.getComment())
                .performedByFullName(performedBy != null ? performedBy.getFullName() : null)
                .performedById(performedBy != null ? performedBy.getId() : null)
                .part(MovementResponseDto.PartInfo.builder()
                        .id(part.getId())
                        .name(part.getName())
                        .sku(part.getSku())
                        .build())
                .build();
    }
}