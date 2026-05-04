package com.example.demo.presentation.controllers;

import com.example.demo.application.serv.UserService;
import com.example.demo.domain.entity.Request;
import com.example.demo.presentation.dto.request.CreateUserDto;
import com.example.demo.presentation.dto.request.SetUserDepartmentDto;
import com.example.demo.presentation.dto.response.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody CreateUserDto dto) {
        UserResponseDto response = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id) {
        UserResponseDto response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/requests")
    public ResponseEntity<List<Request>> getRequestsByUser(@PathVariable UUID id) {
        List<Request> requests = userService.getRequestsByUserId(id);
        return ResponseEntity.ok(requests);
    }

    @PutMapping("/department")
    public ResponseEntity<UserResponseDto> setUserDepartment(@Valid @RequestBody SetUserDepartmentDto dto) {
        UserResponseDto response = userService.setUserDepartment(dto);
        return ResponseEntity.ok(response);
    }
}