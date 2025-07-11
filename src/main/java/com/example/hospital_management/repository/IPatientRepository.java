package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Patient;
import com.example.hospital_management.entity.TestReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IPatientRepository extends JpaRepository<Patient, Long> {
    @Query(value = "SELECT * FROM patients WHERE title LIKE CONCAT('%', :searchName, '%')", nativeQuery = true)
    Page<Patient> searchByName(@Param("searchName") String searchName, Pageable pageable);
}
