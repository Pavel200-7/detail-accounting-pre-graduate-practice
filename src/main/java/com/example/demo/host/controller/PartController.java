package com.example.demo.host.controller;

import com.example.demo.host.controller.data.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/parts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Справочник деталей", description = "API для управления справочником деталей/комплектующих")
public class PartController {

    @Operation(summary = "Получить список всех деталей", description = "Возвращает список всех деталей с возможностью фильтрации по категории")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список успешно получен"),
            @ApiResponse(responseCode = "401", description = "Не авторизован")
    })
    @GetMapping
    public ResponseEntity<List<PartResponse>> getAllParts(
            @Parameter(description = "Фильтр по категории детали", example = "EQUIPMENT")
            @RequestParam(required = false) String category) {
        log.info("GET /api/parts - получение списка деталей (категория: {})", category);

        // TODO: Реализовать вызов сервиса, пока заглушка
        List<PartResponse> mockParts = List.of(
                PartResponse.builder()
                        .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
                        .name("Системный блок HP ProDesk")
                        .sku("PC-HP-001")
                        .unit("PCS")
                        .category("EQUIPMENT")
                        .minStock(2)
                        .price(new BigDecimal("25000.00"))
                        .build(),
                PartResponse.builder()
                        .id(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"))
                        .name("USB-контроллер")
                        .sku("USB-CTRL-01")
                        .unit("PCS")
                        .category("COMPONENT")
                        .minStock(5)
                        .price(new BigDecimal("350.00"))
                        .build()
        );
        return ResponseEntity.ok(mockParts);
    }

    @Operation(summary = "Создать новую деталь", description = "Добавляет новую позицию в справочник. Требует прав администратора или кладовщика.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Деталь создана"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @PostMapping
    public ResponseEntity<PartResponse> createPart(@Valid @RequestBody CreatePartRequest request) {
        log.info("POST /api/parts - создание детали: {}", request.getName());

        // TODO: Реализовать сохранение в БД
        PartResponse response = PartResponse.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .sku(request.getSku())
                .unit(request.getUnit())
                .category(request.getCategory())
                .minStock(request.getMinStock())
                .price(request.getPrice())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}