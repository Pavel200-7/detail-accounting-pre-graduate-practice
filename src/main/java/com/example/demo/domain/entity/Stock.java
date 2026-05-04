package com.example.demo.domain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "stock")
@Data
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Integer quantity = 0;

    @Column(name = "reserved_quantity")
    private Integer reservedQuantity = 0;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id", nullable = false, unique = true)
    private Part part;
}
