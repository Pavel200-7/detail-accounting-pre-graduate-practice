package com.example.demo.presentation.controllers;

import com.example.demo.application.serv.PartService;
import com.example.demo.presentation.dto.request.CreateMovementDto;
import com.example.demo.presentation.dto.request.CreatePartDto;
import com.example.demo.presentation.dto.response.MovementListResponseDto;
import com.example.demo.presentation.dto.response.MovementResponseDto;
import com.example.demo.presentation.dto.response.PartWithStockResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/parts")
@RequiredArgsConstructor
public class PartController {

    private final PartService partService;

    @PostMapping
    public ResponseEntity<PartWithStockResponseDto> createPart(@Valid @RequestBody CreatePartDto dto) {
        PartWithStockResponseDto response = partService.createPart(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/movement")
    public ResponseEntity<MovementResponseDto> createMovement(@Valid @RequestBody CreateMovementDto dto) {
        MovementResponseDto response = partService.createMovement(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartWithStockResponseDto> getPartById(@PathVariable UUID id) {
        PartWithStockResponseDto response = partService.getPartById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PartWithStockResponseDto>> getAllParts() {
        List<PartWithStockResponseDto> response = partService.getAllParts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/movements")
    public ResponseEntity<MovementListResponseDto> getPartWithMovements(@PathVariable UUID id) {
        MovementListResponseDto response = partService.getPartWithMovements(id);
        return ResponseEntity.ok(response);
    }
}