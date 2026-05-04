package com.example.demo.presentation.dto.request;

import com.example.demo.domain.enums.PartCategory;
import com.example.demo.domain.enums.Unit;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreatePartDto {

    @NotBlank(message = "Name не может быть пустым")
    @Size(min = 3, max = 255, message = "Name должен быть от 3 до 255 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9\\s\\-]+$",
            message = "Name может содержать только буквы, цифры, пробелы и дефисы")
    private String name;

    @NotBlank(message = "SKU не может быть пустым")
    @Size(min = 1, max = 100, message = "SKU должен быть от 1 до 100 символов")
    @Pattern(regexp = "^[a-zA-Z0-9\\-]+$",
            message = "SKU может содержать только буквы, цифры и дефисы")
    private String sku;

    @NotNull(message = "Unit обязателен")
    private Unit unit;

    @NotNull(message = "Category обязательна")
    private PartCategory category;

    @NotNull(message = "Price обязателен")
    @DecimalMin(value = "0.01", message = "Price должен быть положительным числом")
    private BigDecimal price;

    @Size(max = 255, message = "Description не длиннее 255 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9\\s\\-\\.,!?]*$",
            message = "Description содержит недопустимые символы")
    private String description;
}