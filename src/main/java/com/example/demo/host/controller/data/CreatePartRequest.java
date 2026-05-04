package com.example.demo.host.controller.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос на создание новой детали")
public class CreatePartRequest {
    @NotBlank(message = "Наименование не может быть пустым")
    @Size(min = 2, max = 255, message = "Наименование должно быть от 2 до 255 символов")
    @Schema(description = "Наименование детали", example = "USB-контроллер")
    private String name;

    @NotBlank(message = "SKU не может быть пустым")
    @Size(max = 100, message = "SKU не может превышать 100 символов")
    @Schema(description = "Артикул детали (уникальный)", example = "USB-CTRL-001")
    private String sku;

    @NotNull(message = "Единица измерения обязательна")
    @Schema(description = "Единица измерения", example = "PCS", allowableValues = {"PCS", "KG", "M", "PACK", "SET", "L"})
    private String unit;

    @Schema(description = "Категория детали", example = "COMPONENT")
    private String category;

    @PositiveOrZero(message = "Минимальный остаток не может быть отрицательным")
    @Schema(description = "Минимальный остаток для оповещения", example = "5", defaultValue = "0")
    private Integer minStock;

    @PositiveOrZero(message = "Цена не может быть отрицательной")
    @Schema(description = "Оценочная стоимость", example = "350.00")
    private BigDecimal price;
}
