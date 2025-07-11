package com.example.hospital_management.repository;

import com.example.hospital_management.entity.EmployeeAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmployeeAssignmentRepository extends JpaRepository<EmployeeAssignment, Long> {
}
