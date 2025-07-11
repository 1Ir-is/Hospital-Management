package com.example.hospital_management.repository;

import com.example.hospital_management.entity.AdvancePayment;
import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.entity.EmployeeAssigment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IEmployeeAssigmentRepository extends JpaRepository<EmployeeAssigment, Long> {
    @Query("""
    SELECT ea.employee FROM EmployeeAssigment ea
    WHERE ea.impatientRecord.id = :recordId
      AND ea.employee.position.name = 'Bác sĩ'
    ORDER BY ea.id DESC
    LIMIT 1
""")
    Optional<Employee> findLatestDoctorByRecordId(@Param("recordId") Long recordId);

    @Query("""
    SELECT ea.employee FROM EmployeeAssigment ea
    WHERE ea.impatientRecord.id = :recordId
      AND ea.employee.position.name = 'Điều dưỡng'
    ORDER BY ea.id DESC
    LIMIT 1
""")
    Optional<Employee> findLatestNurseByRecordId(@Param("recordId") Long recordId);
}
