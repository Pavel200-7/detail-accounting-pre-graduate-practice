package com.example.demo.presentation.controllers;

import com.example.demo.application.serv.DepartmentService;
import com.example.demo.domain.entity.User;
import com.example.demo.presentation.dto.request.CreateDepartmentDto;
import com.example.demo.presentation.dto.request.UpdateDepartmentDto;
import com.example.demo.presentation.dto.response.DepartmentResponseDto;
import com.example.demo.presentation.dto.response.DepartmentTreeDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentResponseDto> createDepartment(
            @Valid @RequestBody CreateDepartmentDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(departmentService.createDepartment(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> updateDepartment(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDepartmentDto dto) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponseDto> getDepartmentById(@PathVariable UUID id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @GetMapping
    public ResponseEntity<List<DepartmentTreeDto>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartmentsHierarchy());
    }

    @GetMapping("/{id}/users")
    public ResponseEntity<List<User>> getUsersByDepartment(@PathVariable UUID id) {
        return ResponseEntity.ok(departmentService.getUsersByDepartmentId(id));
    }
}