package com.example.demo.infrastructure.repositories;

import com.example.demo.domain.entity.RequestItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface RequestItemRepository extends JpaRepository<RequestItem, UUID> {
}