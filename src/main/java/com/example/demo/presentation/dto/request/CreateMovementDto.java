package com.example.demo.presentation.dto.request;

import com.example.demo.domain.enums.MovementType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateMovementDto {

    @NotNull(message = "PartId обязателен")
    private UUID partId;

    @NotNull(message = "Type обязателен")
    private MovementType type;

    @NotNull(message = "Quantity обязателен")
    @Min(value = 1, message = "Quantity должен быть положительным числом")
    private Integer quantity;

    @Size(max = 50, message = "DocumentType не длиннее 50 символов")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-]*$",
            message = "DocumentType может содержать только буквы, цифры, пробелы и дефисы")
    private String documentType;

    @Size(max = 255, message = "Comment не длиннее 255 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9\\s\\-\\.,!?]*$",
            message = "Comment содержит недопустимые символы")
    private String comment;
}