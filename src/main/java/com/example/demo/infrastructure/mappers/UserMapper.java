package com.example.demo.infrastructure.mappers;

import com.example.demo.domain.entity.User;
import com.example.demo.presentation.dto.request.CreateUserDto;
import com.example.demo.presentation.dto.response.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(CreateUserDto dto) {
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        return user;
    }

    public UserResponseDto toResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .departmentId(user.getDepartment() != null ? user.getDepartment().getId() : null)
                .departmentName(user.getDepartment() != null ? user.getDepartment().getName() : null)
                .createdAt(user.getCreatedAt())
                .build();
    }
}