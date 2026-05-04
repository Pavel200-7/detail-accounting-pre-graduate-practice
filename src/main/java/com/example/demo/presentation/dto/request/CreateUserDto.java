package com.example.demo.presentation.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserDto {

    @NotBlank(message = "FullName не может быть пустым")
    @Size(min = 3, max = 255, message = "FullName должен быть от 3 до 255 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\\s\\-]+$",
            message = "FullName может содержать только буквы, пробелы и дефисы")
    private String fullName;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Неверный формат email")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Неверный формат email")
    private String email;

    @NotBlank(message = "Phone не может быть пустым")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$",
            message = "Phone должен содержать 10-15 цифр, может начинаться с +")
    private String phone;
}