package com.example.hospital_management.repository;

import com.example.hospital_management.entity.TestOrder;
import com.example.hospital_management.entity.TestReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ITestReportRepository extends JpaRepository<TestReport, Long> {
    //    @Query(value = "SELECT * FROM test_reports WHERE title LIKE CONCAT('%', :searchName, '%')", nativeQuery = true)
//    Page<TestReport> searchByName(@Param("searchName") String searchName, Pageable pageable);
    @Query("SELECT tr FROM TestReport tr " +
            "WHERE (:searchName IS NULL OR LOWER(tr.medicalRecord.patient.name) LIKE LOWER(CONCAT('%', :searchName, '%'))) " +
            "AND (:medicalRecordId IS NULL OR tr.medicalRecord.id = :medicalRecordId)")
    Page<TestReport> searchByName(@Param("searchName") String searchName,
                                  @Param("medicalRecordId") Long medicalRecordId,
                                  Pageable pageable);

}
