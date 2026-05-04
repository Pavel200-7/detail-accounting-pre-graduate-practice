package com.example.demo.infrastructure.repositories;

import com.example.demo.domain.entity.Movement;
import com.example.demo.domain.enums.MovementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovementRepository extends JpaRepository<Movement, UUID> {
    List<Movement> findByPartIdOrderByPerformedAtDesc(UUID partId);
    List<Movement> findByType(MovementType type);
}