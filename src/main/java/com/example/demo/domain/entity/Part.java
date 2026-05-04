package com.example.demo.domain.entity;

import com.example.demo.domain.enums.PartCategory;
import com.example.demo.domain.enums.Unit;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "parts")
@Data
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(unique = true, length = 100)
    private String sku; // артикул, уникальный код

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Unit unit; // единица измерения

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private PartCategory category;

    @Column(name = "min_stock")
    private Integer minStock = 0; // минимальный остаток для оповещения

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Навигационное поле: остаток на складе (One-to-One)
    @OneToOne(mappedBy = "part", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Stock stock;

    // Навигационное поле: позиции в заявках
    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RequestItem> requestItems = new ArrayList<>();

    // Навигационное поле: движения
    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Movement> movements = new ArrayList<>();

    // Геттеры, сеттеры, equals, hashCode
}