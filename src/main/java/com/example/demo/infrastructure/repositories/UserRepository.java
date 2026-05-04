package com.example.demo.infrastructure.repositories;

import com.example.demo.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    List<User> findByDepartmentId(UUID departmentId);


    @Query("SELECT u FROM User u WHERE u.department.id = :departmentId")
    List<User> findAllByDepartmentId(@Param("departmentId") UUID departmentId);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.department WHERE u.id = :userId")
    Optional<User> findByIdWithDepartment(@Param("userId") UUID userId);
}