package com.example.demo.host.controller.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание заявки")
public class CreateRequestRequest {
    @NotEmpty(message = "Заявка не может быть пустой")
    @Valid
    @Schema(description = "Список позиций заявки")
    private List<RequestItemDto> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Позиция заявки")
    public static class RequestItemDto {
        @NotNull(message = "ID детали обязателен")
        private UUID partId;

        @Positive(message = "Количество должно быть положительным")
        private Integer quantity;

        private String comment;
    }
}

