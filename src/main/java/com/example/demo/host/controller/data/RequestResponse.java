package com.example.demo.host.controller.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO заявки")
public class RequestResponse {
    private UUID id;
    @Schema(description = "Номер заявки", example = "REQ-20260327-00001")
    private String number;
    private String status;
    private String employeeName;
    private String departmentName;
    private List<RequestItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime executedAt;
    private String rejectionReason;
}
