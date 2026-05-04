package com.example.demo.presentation.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateDepartmentDto {

    @NotBlank(message = "Name не может быть пустым")
    @Size(min = 3, max = 255, message = "Name должен быть от 3 до 255 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9\\s\\-]+$",
            message = "Name может содержать только буквы, цифры, пробелы и дефисы")
    private String name;

    @Size(max = 255, message = "Description не длиннее 255 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9\\s\\-\\.,!?]*$",
            message = "Description содержит недопустимые символы")
    private String description;
}