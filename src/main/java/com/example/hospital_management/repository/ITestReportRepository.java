package com.example.hospital_management.repository;

import com.example.hospital_management.entity.TestReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITestReportRepository extends JpaRepository<TestReport, Long> {
    @Query("""
        SELECT tr FROM TestReport tr WHERE tr.medicalRecord.id = :medicalRecordId   
    """)
    List<TestReport> findByMedicalRecordId(@Param("medicalRecordId") Long id);
}
