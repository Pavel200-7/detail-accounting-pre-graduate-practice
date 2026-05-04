package com.example.demo.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class SetUserDepartmentDto {

    @NotNull(message = "Id пользователя обязателен")
    private UUID id;

    @NotNull(message = "DepartmentId обязателен")
    private UUID departmentId;
}