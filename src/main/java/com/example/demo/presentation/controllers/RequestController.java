package com.example.demo.presentation.controllers;

import com.example.demo.application.serv.RequestService;
import com.example.demo.domain.enums.RequestStatus;
import com.example.demo.presentation.dto.request.CreateRequestDto;
import com.example.demo.presentation.dto.request.RejectRequestDto;
import com.example.demo.presentation.dto.response.RequestWithItemsResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<RequestWithItemsResponseDto> createRequest(@Valid @RequestBody CreateRequestDto dto) {
        RequestWithItemsResponseDto response = requestService.createRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<RequestWithItemsResponseDto> approveRequest(@PathVariable UUID id) {
        RequestWithItemsResponseDto response = requestService.approveRequest(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reject")
    public ResponseEntity<RequestWithItemsResponseDto> rejectRequest(@Valid @RequestBody RejectRequestDto dto) {
        RequestWithItemsResponseDto response = requestService.rejectRequest(dto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<RequestWithItemsResponseDto> completeRequest(@PathVariable UUID id) {
        RequestWithItemsResponseDto response = requestService.completeRequest(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestWithItemsResponseDto> getRequestById(@PathVariable UUID id) {
        RequestWithItemsResponseDto response = requestService.getRequestById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RequestWithItemsResponseDto>> getRequestsByUserId(@PathVariable UUID userId) {
        List<RequestWithItemsResponseDto> response = requestService.getRequestsByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RequestWithItemsResponseDto>> getRequestsByStatus(@PathVariable RequestStatus status) {
        List<RequestWithItemsResponseDto> response = requestService.getRequestsByStatus(status);
        return ResponseEntity.ok(response);
    }
}