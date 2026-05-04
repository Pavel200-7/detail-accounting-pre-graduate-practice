package com.example.demo.presentation.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateDepartmentDto {

    @NotBlank(message = "Name не может быть пустым")
    @Size(min = 3, max = 255, message = "Name должен быть от 3 до 255 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9\\s\\-]+$",
            message = "Name может содержать только буквы, цифры, пробелы и дефисы")
    private String name;

    private UUID parentId;

    @NotBlank(message = "Code не может быть пустым")
    @Size(min = 1, max = 16, message = "Code должен быть от 1 до 16 символов")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$",
            message = "Code может содержать только буквы, цифры и знак подчеркивания")
    private String code;

    @Size(max = 255, message = "Description не длиннее 255 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9\\s\\-\\.,!?]*$",
            message = "Description содержит недопустимые символы")
    private String description;
}
