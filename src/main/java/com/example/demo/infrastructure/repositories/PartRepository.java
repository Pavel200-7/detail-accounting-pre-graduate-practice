package com.example.demo.infrastructure.repositories;

import com.example.demo.domain.entity.Part;
import com.example.demo.domain.enums.PartCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartRepository extends JpaRepository<Part, UUID> {
    Optional<Part> findBySku(String sku);
    List<Part> findByCategory(PartCategory category);
    boolean existsByName(String name);
    boolean existsBySku(String sku);
}