package com.example.demo.presentation.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.UUID;

@Data
@Builder
public class DepartmentResponseDto {
    private UUID id;
    private String name;
    private UUID parentId;
    private String code;
    private String description;
}