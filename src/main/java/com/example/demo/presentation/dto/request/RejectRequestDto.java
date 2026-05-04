package com.example.demo.presentation.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.UUID;

@Data
public class RejectRequestDto {

    @NotNull(message = "Id обязателен")
    private UUID id;

    @NotBlank(message = "RejectionReason не может быть пустым")
    @Size(max = 255, message = "RejectionReason не длиннее 255 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9\\s\\-\\.,!?]*$",
            message = "RejectionReason содержит недопустимые символы")
    private String rejectionReason;
}