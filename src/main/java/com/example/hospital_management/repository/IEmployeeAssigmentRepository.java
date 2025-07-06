package com.example.hospital_management.repository;

import com.example.hospital_management.entity.AdvancePayment;
import com.example.hospital_management.entity.EmployeeAssigment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmployeeAssigmentRepository extends JpaRepository<EmployeeAssigment, Long> {
}
