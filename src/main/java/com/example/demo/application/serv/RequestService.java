package com.example.demo.application.serv;

import com.example.demo.domain.enums.RequestStatus;
import com.example.demo.presentation.dto.request.CreateRequestDto;
import com.example.demo.presentation.dto.request.RejectRequestDto;
import com.example.demo.presentation.dto.response.RequestWithItemsResponseDto;

import java.util.List;
import java.util.UUID;

public interface RequestService {
    RequestWithItemsResponseDto createRequest(CreateRequestDto dto);
    RequestWithItemsResponseDto approveRequest(UUID id);
    RequestWithItemsResponseDto rejectRequest(RejectRequestDto dto);
    RequestWithItemsResponseDto completeRequest(UUID id);
    RequestWithItemsResponseDto getRequestById(UUID id);
    List<RequestWithItemsResponseDto> getRequestsByUserId(UUID userId);
    List<RequestWithItemsResponseDto> getRequestsByStatus(RequestStatus status);
}