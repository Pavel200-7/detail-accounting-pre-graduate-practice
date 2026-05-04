package com.example.demo.infrastructure.mappers;

import com.example.demo.domain.entity.*;
import com.example.demo.presentation.dto.request.CreateRequestDto;
import com.example.demo.presentation.dto.response.RequestWithItemsResponseDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RequestMapper {

    public Request toEntity(CreateRequestDto dto, User employee, Department department) {
        Request request = new Request();
        request.setComment(dto.getComment());
        request.setEmployee(employee);
        request.setDepartment(department);
        return request;
    }

    public RequestWithItemsResponseDto toResponseDto(Request request) {
        return RequestWithItemsResponseDto.builder()
                .id(request.getId())
                .status(request.getStatus())
                .comment(request.getComment())
                .rejectionReason(request.getRejectionReason())
                .createdAt(request.getCreatedAt())
                .submittedAt(request.getSubmittedAt())
                .approvedAt(request.getApprovedAt())
                .executedAt(request.getExecutedAt())
                .employee(RequestWithItemsResponseDto.UserInfo.builder()
                        .id(request.getEmployee().getId())
                        .fullName(request.getEmployee().getFullName())
                        .email(request.getEmployee().getEmail())
                        .build())
                .department(RequestWithItemsResponseDto.DepartmentInfo.builder()
                        .id(request.getDepartment().getId())
                        .name(request.getDepartment().getName())
                        .build())
                .items(request.getItems().stream()
                        .map(this::toItemInfo)
                        .collect(Collectors.toList()))
                .build();
    }

    private RequestWithItemsResponseDto.RequestItemInfo toItemInfo(RequestItem item) {
        Stock stock = item.getPart().getStock();
        int availableQuantity = stock != null ?
                stock.getQuantity() - stock.getReservedQuantity() : 0;
        int reservedQuantity = stock != null ?
                stock.getReservedQuantity() : 0;

        return RequestWithItemsResponseDto.RequestItemInfo.builder()
                .id(item.getId())
                .quantity(item.getQuantity())
                .part(RequestWithItemsResponseDto.RequestItemInfo.PartInfo.builder()
                        .id(item.getPart().getId())
                        .name(item.getPart().getName())
                        .sku(item.getPart().getSku())
                        .availableQuantity(availableQuantity)
                        .reservedQuantity(reservedQuantity)
                        .build())
                .build();
    }
}