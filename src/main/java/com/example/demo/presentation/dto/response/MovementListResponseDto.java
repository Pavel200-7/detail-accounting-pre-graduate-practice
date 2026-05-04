package com.example.demo.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MovementListResponseDto {
    private PartWithStockResponseDto part;
    private List<MovementResponseDto> movements;
}