package com.example.demo.host.controller;

import com.example.demo.host.controller.data.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Складской учет", description = "API для управления остатками, приемкой и списанием деталей")
public class StockController {

    @Operation(summary = "Получить остатки всех деталей", description = "Возвращает список с текущим количеством деталей на складе.")
    @GetMapping
    public ResponseEntity<List<StockResponse>> getAllStock() {
        log.info("GET /api/stock - получение остатков");
        return ResponseEntity.ok(List.of());
    }

    @Operation(summary = "Приемка деталей на склад", description = "Увеличивает остатки. Требует роль STOREKEEPER или ADMIN.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Приемка выполнена"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping("/receipt")
    public ResponseEntity<Void> receiptParts(
            @Valid @RequestBody ReceiptRequest request) {
        // TODO: Вызов сервиса для увеличения остатков
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Списание деталей", description = "Уменьшает остатки (акт списания). Требует роль STOREKEEPER или ADMIN.")
    @PostMapping("/write-off")
    public ResponseEntity<Void> writeOffParts(
            @RequestBody @Valid ReceiptRequest request) {
        log.info("POST /api/stock/write-off - списание детали {}", request.getPartId());
        return ResponseEntity.ok().build();
    }

}