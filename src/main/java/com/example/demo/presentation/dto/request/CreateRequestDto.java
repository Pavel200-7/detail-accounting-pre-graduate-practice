package com.example.demo.presentation.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class CreateRequestDto {

    @Size(max = 255, message = "Comment не длиннее 255 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9\\s\\-\\.,!?]*$",
            message = "Comment содержит недопустимые символы")
    private String comment;

    @NotNull(message = "Items не может быть null")
    @Size(min = 1, message = "Должна быть хотя бы одна позиция")
    private List<RequestItemDto> items;

    @Data
    public static class RequestItemDto {
        @NotNull(message = "PartId обязателен")
        private UUID partId;

        @NotNull(message = "Quantity обязателен")
        @Min(value = 1, message = "Quantity должен быть положительным числом")
        private Integer quantity;
    }
}