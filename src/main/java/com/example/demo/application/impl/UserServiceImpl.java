package com.example.demo.application.impl;

import com.example.demo.application.serv.UserService;
import com.example.demo.domain.entity.Department;
import com.example.demo.domain.entity.Request;
import com.example.demo.domain.entity.User;
import com.example.demo.infrastructure.exceptions.BusinessRuleException;
import com.example.demo.infrastructure.exceptions.EntityNotFoundException;
import com.example.demo.infrastructure.mappers.UserMapper;
import com.example.demo.infrastructure.repositories.DepartmentRepository;
import com.example.demo.infrastructure.repositories.RequestRepository;
import com.example.demo.infrastructure.repositories.UserRepository;
import com.example.demo.presentation.dto.request.CreateUserDto;
import com.example.demo.presentation.dto.request.SetUserDepartmentDto;
import com.example.demo.presentation.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final RequestRepository requestRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto createUser(CreateUserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessRuleException(
                    String.format("Пользователь с email '%s' уже существует", dto.getEmail())
            );
        }

        if (userRepository.existsByPhone(dto.getPhone())) {
            throw new BusinessRuleException(
                    String.format("Пользователь с phone '%s' уже существует", dto.getPhone())
            );
        }

        User user = userMapper.toEntity(dto);
        user.setCreatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);
        return userMapper.toResponseDto(saved);
    }

    @Override
    public UserResponseDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));
        return userMapper.toResponseDto(user);
    }

    @Override
    public List<Request> getRequestsByUserId(UUID id) {
        userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id));
        return requestRepository.findByEmployeeId(id);
    }

    @Override
    @Transactional
    public UserResponseDto setUserDepartment(SetUserDepartmentDto dto) {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User", dto.getId()));
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("Department", dto.getDepartmentId()));

        user.setDepartment(department);

        User updated = userRepository.save(user);
        return userMapper.toResponseDto(updated);
    }
}