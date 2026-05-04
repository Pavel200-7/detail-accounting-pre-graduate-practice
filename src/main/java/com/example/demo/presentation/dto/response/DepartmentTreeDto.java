package com.example.demo.presentation.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class DepartmentTreeDto {
    private UUID id;
    private String name;
    private String code;
    private String description;
    private List<DepartmentTreeDto> children;
}