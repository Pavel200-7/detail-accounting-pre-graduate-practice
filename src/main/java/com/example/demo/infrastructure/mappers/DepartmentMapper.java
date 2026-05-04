package com.example.demo.infrastructure.mappers;

import com.example.demo.domain.entity.Department;
import com.example.demo.presentation.dto.request.CreateDepartmentDto;
import com.example.demo.presentation.dto.response.DepartmentResponseDto;
import com.example.demo.presentation.dto.response.DepartmentTreeDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepartmentMapper {

    public Department toEntity(CreateDepartmentDto dto, Department parent) {
        Department department = new Department();
        department.setName(dto.getName());
        department.setParent(parent);
        department.setCode(dto.getCode());
        department.setDescription(dto.getDescription());
        return department;
    }

    public DepartmentResponseDto toResponseDto(Department department) {
        return DepartmentResponseDto.builder()
                .id(department.getId())
                .name(department.getName())
                .parentId(department.getParent() != null ? department.getParent().getId() : null)
                .code(department.getCode())
                .description(department.getDescription())
                .build();
    }

    public DepartmentTreeDto toTreeDto(Department department) {
        return DepartmentTreeDto.builder()
                .id(department.getId())
                .name(department.getName())
                .code(department.getCode())
                .description(department.getDescription())
                .children(
                        department.getChildren() != null ?
                                department.getChildren().stream()
                                        .map(this::toTreeDto)
                                        .collect(Collectors.toList()) :
                                List.of()
                )
                .build();
    }
}