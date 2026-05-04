package com.example.demo.application.serv;

import com.example.demo.presentation.dto.request.CreateMovementDto;
import com.example.demo.presentation.dto.request.CreatePartDto;
import com.example.demo.presentation.dto.response.MovementListResponseDto;
import com.example.demo.presentation.dto.response.MovementResponseDto;
import com.example.demo.presentation.dto.response.PartWithStockResponseDto;

import java.util.List;
import java.util.UUID;

public interface PartService {
    PartWithStockResponseDto createPart(CreatePartDto dto);
    MovementResponseDto createMovement(CreateMovementDto dto);
    PartWithStockResponseDto getPartById(UUID id);
    List<PartWithStockResponseDto> getAllParts();
    MovementListResponseDto getPartWithMovements(UUID id);
}