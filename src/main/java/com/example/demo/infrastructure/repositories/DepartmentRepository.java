package com.example.demo.infrastructure.repositories;

import com.example.demo.domain.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    boolean existsByNameAndParentId(String name, UUID parentId);
    boolean existsByNameAndParentIdAndIdNot(String name, UUID parentId, UUID excludeId);
    boolean existsByCode(String code);
    boolean existsByParentId(UUID parentId);

    @Query("SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.children WHERE d.parent IS NULL")
    List<Department> findAllRootDepartmentsWithChildren();


    @Query("SELECT COUNT(r) > 0 FROM Request r WHERE r.department.id = :departmentId")
    boolean hasRequests(@Param("departmentId") UUID departmentId);
}