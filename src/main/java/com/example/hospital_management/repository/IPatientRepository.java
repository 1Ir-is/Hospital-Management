package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Patient;
import com.example.hospital_management.entity.TestReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IPatientRepository extends JpaRepository<Patient, Long> {
    @Query("""
    SELECT DISTINCT p FROM Patient p
    JOIN MedicalRecord mr ON mr.patient = p
    JOIN ImpatientRecord ir ON ir.medicalRecord = mr
    JOIN Bed b ON b = ir.bed
    JOIN Room r ON r = b.room
    LEFT JOIN EmployeeAssignment ea ON ea.impatientRecord = ir
    LEFT JOIN Employee e ON ea.employee = e
    WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:roomName IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', :roomName, '%')))
      AND (:doctorName IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :doctorName, '%')))
""")
    List<Patient> searchPatients(
            @Param("name") String name,
            @Param("roomName") String roomName,
            @Param("doctorName") String doctorName
    );


    List<Patient> findAllByIdCard(String idCard);
    Patient findPatientById(Long id);
    @Query(value = "SELECT * FROM patients WHERE title LIKE CONCAT('%', :searchName, '%')", nativeQuery = true)
    Page<Patient> searchByName(@Param("searchName") String searchName, Pageable pageable);
}
