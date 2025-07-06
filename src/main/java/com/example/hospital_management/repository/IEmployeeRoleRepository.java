package com.example.hospital_management.repository;

import com.example.hospital_management.entity.AdvancePayment;
import com.example.hospital_management.entity.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmployeeRoleRepository extends JpaRepository<EmployeeRole, Long> {
}
