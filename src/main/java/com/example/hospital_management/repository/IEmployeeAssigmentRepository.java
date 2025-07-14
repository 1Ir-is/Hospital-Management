package com.example.hospital_management.repository;

import com.example.hospital_management.entity.AdvancePayment;
import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.entity.EmployeeAssignment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IEmployeeAssigmentRepository extends JpaRepository<EmployeeAssignment, Long> {
    @Query("""
    SELECT ea.employee FROM EmployeeAssignment ea
    WHERE ea.impatientRecord.id = :recordId
      AND ea.employee.position.name = 'Bác sĩ'
    ORDER BY ea.id DESC
    LIMIT 1
""")
    Optional<Employee> findLatestDoctorByRecordId(@Param("recordId") Long recordId);

    @Query("""
    SELECT ea.employee FROM EmployeeAssignment ea
    WHERE ea.impatientRecord.id = :recordId
      AND ea.employee.position.name = 'Điều dưỡng'
    ORDER BY ea.id DESC
    LIMIT 1
""")
    Optional<Employee> findLatestNurseByRecordId(@Param("recordId") Long recordId);


    @Query(value = "SELECT * FROM employee_assignments ea WHERE ea.impatient_record_id = :id",nativeQuery = true)
    List<EmployeeAssignment> findByImpatientRecordId(@Param("id") Long id);

    @Query(value = "SELECT * FROM employee_assignments ea  WHERE ea.impatient_record_id  = :inpatientRecordId", nativeQuery = true)
    List<EmployeeAssignment> findByInpatientRecordId(@Param("impatientRecordId") Long impatientRecordId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM employee_assignments WHERE impatient_record_id = :inpatientRecordId", nativeQuery = true)
    void deleteByInpatientRecordId(@Param("inpatientRecordId") Long inpatientRecordId);
}
