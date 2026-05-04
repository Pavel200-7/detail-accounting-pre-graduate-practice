package com.example.demo.application.serv;

import com.example.demo.domain.entity.User;
import com.example.demo.presentation.dto.request.CreateDepartmentDto;
import com.example.demo.presentation.dto.request.UpdateDepartmentDto;
import com.example.demo.presentation.dto.response.DepartmentResponseDto;
import com.example.demo.presentation.dto.response.DepartmentTreeDto;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    DepartmentResponseDto createDepartment(CreateDepartmentDto dto);
    void deleteDepartment(UUID id);
    DepartmentResponseDto updateDepartment(UUID id, UpdateDepartmentDto dto);
    DepartmentResponseDto getDepartmentById(UUID id);
    List<DepartmentTreeDto> getAllDepartmentsHierarchy();
    List<User> getUsersByDepartmentId(UUID id);
}