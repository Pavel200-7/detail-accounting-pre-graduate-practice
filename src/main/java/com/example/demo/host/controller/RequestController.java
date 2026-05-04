package com.example.demo.host.controller;

import com.example.demo.host.controller.data.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Управление заявками", description = "API для создания, согласования и выполнения заявок на детали")
public class RequestController {

    @Operation(summary = "Создать новую заявку", description = "Создает черновик заявки. Доступно для роли EMPLOYEE и выше.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Заявка создана"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации")
    })
    @PostMapping
    public ResponseEntity<RequestResponse> createRequest(
            @Valid @RequestBody CreateRequestRequest request) {
        UUID currentId = UUID.randomUUID();
        log.info("POST /api/requests - пользователь {} создает заявку", currentId);

        // TODO: Реализовать вызов сервиса
        RequestResponse mockResponse = RequestResponse.builder()
                .id(UUID.randomUUID())
                .number("REQ-20260327-00001")
                .status("DRAFT")
                .employeeName(currentId.toString())
                .departmentName("Кафедра ИТ")
                .createdAt(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(mockResponse);
    }

    @Operation(summary = "Отправить заявку на согласование", description = "Переводит заявку в статус PENDING. Доступно автору заявки.")
    @PutMapping("/{id}/submit")
    public ResponseEntity<RequestResponse> submitRequest(
            @Parameter(description = "ID заявки", required = true) @PathVariable UUID id) {
        log.info("PUT /api/requests/{}/submit - отправка на согласование", id);
        return ResponseEntity.ok(RequestResponse.builder().id(id).status("PENDING").build());
    }

    @Operation(summary = "Утвердить заявку", description = "Переводит заявку в статус APPROVED. Требует роль MANAGER.")
    @PutMapping("/{id}/approve")
    public ResponseEntity<RequestResponse> approveRequest(
            @PathVariable UUID id) {
        UUID currentId = UUID.randomUUID();
        log.info("PUT /api/requests/{}/approve - утверждение руководителем {}", id, currentId);
        return ResponseEntity.ok(RequestResponse.builder().id(id).status("APPROVED").build());
    }

    @Operation(summary = "Выполнить заявку", description = "Списывает детали со склада. Требует роль STOREKEEPER.")
    @PutMapping("/{id}/execute")
    public ResponseEntity<RequestResponse> executeRequest(
            @PathVariable UUID id) {
        UUID currentId = UUID.randomUUID();
        log.info("PUT /api/requests/{}/execute - выполнение кладовщиком {}", id, currentId);
        return ResponseEntity.ok(RequestResponse.builder().id(id).status("COMPLETED").build());
    }

}