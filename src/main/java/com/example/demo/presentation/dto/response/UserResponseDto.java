package com.example.demo.presentation.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserResponseDto {
    private UUID id;
    private String fullName;
    private String email;
    private String phone;
    private UUID departmentId;
    private String departmentName;
    private LocalDateTime createdAt;
}