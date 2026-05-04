package com.example.demo.domain.entity;

import com.example.demo.domain.enums.MovementType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "movements")
@Data
public class Movement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private MovementType type;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "quantity_before")
    private Integer quantityBefore;

    @Column(name = "quantity_after")
    private Integer quantityAfter;

    @Column(name = "document_type", length = 50)
    private String documentType;

    @Column(name = "performed_at")
    private LocalDateTime performedAt;

    @Column(length = 255)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by")
    private User performedBy;
}