package com.example.demo.application.impl;

import com.example.demo.application.serv.DepartmentService;
import com.example.demo.domain.entity.Department;
import com.example.demo.domain.entity.User;
import com.example.demo.infrastructure.exceptions.BusinessRuleException;
import com.example.demo.infrastructure.exceptions.EntityNotFoundException;
import com.example.demo.infrastructure.mappers.DepartmentMapper;
import com.example.demo.infrastructure.repositories.DepartmentRepository;
import com.example.demo.infrastructure.repositories.UserRepository;
import com.example.demo.presentation.dto.request.CreateDepartmentDto;
import com.example.demo.presentation.dto.request.UpdateDepartmentDto;
import com.example.demo.presentation.dto.response.DepartmentResponseDto;
import com.example.demo.presentation.dto.response.DepartmentTreeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    @Transactional
    public DepartmentResponseDto createDepartment(CreateDepartmentDto dto) {
        UUID parentId = dto.getParentId();

        if (departmentRepository.existsByNameAndParentId(dto.getName(), parentId)) {
            throw new BusinessRuleException(
                    String.format("Department с name '%s' уже существует в рамках родителя с id '%s'",
                            dto.getName(), parentId)
            );
        }

        if (departmentRepository.existsByCode(dto.getCode())) {
            throw new BusinessRuleException(
                    String.format("Department с code '%s' уже существует", dto.getCode())
            );
        }

        Department parent = null;
        if (parentId != null) {
            parent = departmentRepository.findById(parentId)
                    .orElseThrow(() -> new EntityNotFoundException("Department", parentId));
        }

        Department department = departmentMapper.toEntity(dto, parent);

        Department saved = departmentRepository.save(department);
        return departmentMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public void deleteDepartment(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department", id));

        if (departmentRepository.existsByParentId(id)) {
            throw new BusinessRuleException("Нельзя удалить Department, у которого есть дочерние подразделения");
        }

        if (departmentRepository.hasRequests(id)) {
            throw new BusinessRuleException("Нельзя удалить Department, к которому привязаны заявки");
        }

        if (department.getUsers() != null && !department.getUsers().isEmpty()) {
            throw new BusinessRuleException("Нельзя удалить Department, в котором есть пользователи");
        }

        departmentRepository.delete(department);
    }

    @Override
    @Transactional
    public DepartmentResponseDto updateDepartment(UUID id, UpdateDepartmentDto dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department", id));

        UUID parentId = department.getParent() != null ? department.getParent().getId() : null;
        if (departmentRepository.existsByNameAndParentIdAndIdNot(dto.getName(), parentId, id)) {
            throw new BusinessRuleException(
                    String.format("Department с name '%s' уже существует в рамках текущего родителя", dto.getName())
            );
        }

        department.setName(dto.getName());
        department.setDescription(dto.getDescription());

        Department updated = departmentRepository.save(department);
        return departmentMapper.toResponseDto(updated);
    }

    @Override
    public DepartmentResponseDto getDepartmentById(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department", id));
        return departmentMapper.toResponseDto(department);
    }

    @Override
    public List<DepartmentTreeDto> getAllDepartmentsHierarchy() {
        List<Department> rootDepartments = departmentRepository.findAllRootDepartmentsWithChildren();
        return rootDepartments.stream()
                .map(departmentMapper::toTreeDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUsersByDepartmentId(UUID id) {
        departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department", id));
        return userRepository.findAllByDepartmentId(id);
    }
}