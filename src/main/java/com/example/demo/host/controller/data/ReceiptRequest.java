package com.example.demo.host.controller.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на приемку деталей на склад")
public class ReceiptRequest {
    @NotNull(message = "ID детали обязателен")
    private UUID partId;

    @Positive(message = "Количество должно быть положительным")
    private Integer quantity;

    @Schema(description = "Номер накладной или документа-основания", example = "ТОРГ-12 №456")
    private String documentNumber;

    private String comment;
}
