package com.example.demo.infrastructure.repositories;

import com.example.demo.domain.entity.Request;
import com.example.demo.domain.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestRepository extends JpaRepository<Request, UUID> {
    List<Request> findByEmployeeId(UUID employeeId);
    List<Request> findByDepartmentId(UUID departmentId);
    List<Request> findByStatus(RequestStatus status);

    @Query("SELECT DISTINCT r FROM Request r LEFT JOIN FETCH r.items i LEFT JOIN FETCH i.part p LEFT JOIN FETCH p.stock WHERE r.id = :id")
    Optional<Request> findByIdWithItems(@Param("id") UUID id);

    @Query("SELECT DISTINCT r FROM Request r LEFT JOIN FETCH r.items i LEFT JOIN FETCH i.part p LEFT JOIN FETCH p.stock WHERE r.employee.id = :employeeId")
    List<Request> findByEmployeeIdWithItems(@Param("employeeId") UUID employeeId);

    @Query("SELECT DISTINCT r FROM Request r LEFT JOIN FETCH r.items i LEFT JOIN FETCH i.part p LEFT JOIN FETCH p.stock WHERE r.status = :status")
    List<Request> findByStatusWithItems(@Param("status") RequestStatus status);
}
