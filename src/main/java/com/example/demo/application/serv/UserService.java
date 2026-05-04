package com.example.demo.application.serv;

import com.example.demo.domain.entity.Request;
import com.example.demo.presentation.dto.request.CreateUserDto;
import com.example.demo.presentation.dto.request.SetUserDepartmentDto;
import com.example.demo.presentation.dto.response.UserResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDto createUser(CreateUserDto dto);
    UserResponseDto getUserById(UUID id);
    List<Request> getRequestsByUserId(UUID id);
    UserResponseDto setUserDepartment(SetUserDepartmentDto dto);
}