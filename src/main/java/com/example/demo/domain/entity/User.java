package com.example.demo.domain.entity;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "full_name", nullable = false, length = 255)
    private String fullName;

    private String email;
    private String phone;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Навигационное поле: подразделение
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    // Созданные заявки
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("createdAt DESC")
    private List<Request> createdRequests = new ArrayList<>();

    // Выполненные движения
    @OneToMany(mappedBy = "performedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Movement> performedMovements = new ArrayList<>();
}