package com.example.demo.presentation.dto.response;

import com.example.demo.domain.enums.RequestStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class RequestWithItemsResponseDto {
    private UUID id;
    private RequestStatus status;
    private String comment;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime executedAt;
    private UserInfo employee;
    private DepartmentInfo department;
    private List<RequestItemInfo> items;

    @Data
    @Builder
    public static class UserInfo {
        private UUID id;
        private String fullName;
        private String email;
    }

    @Data
    @Builder
    public static class DepartmentInfo {
        private UUID id;
        private String name;
    }

    @Data
    @Builder
    public static class RequestItemInfo {
        private UUID id;
        private Integer quantity;
        private PartInfo part;

        @Data
        @Builder
        public static class PartInfo {
            private UUID id;
            private String name;
            private String sku;
            private Integer availableQuantity;
            private Integer reservedQuantity;
        }
    }
}