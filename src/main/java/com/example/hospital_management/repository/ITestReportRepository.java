package com.example.hospital_management.repository;

import com.example.hospital_management.dto.TestRequestDto;
import com.example.hospital_management.entity.TestReport;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ITestReportRepository extends JpaRepository<TestReport, Long> {
    @Query("""
        SELECT tr FROM TestReport tr WHERE tr.medicalRecord.id = :medicalRecordId   
    """)
    List<TestReport> findByMedicalRecordId(@Param("medicalRecordId") Long id);
    @Query(value = """
                SELECT 
                    tr.id AS id,
                    p.name AS patientName,
                    p.id AS patientId,
                    t.name AS name,
                    tr.note AS note,
                    ts.name AS status
                FROM test_reports tr
                JOIN tests t ON tr.test_id = t.id
                JOIN medical_records mr ON tr.medical_record_id = mr.id
                JOIN patients p ON mr.patient_id = p.id
                JOIN test_status ts ON tr.test_status_id = ts.id
                WHERE t.room_id = :roomId
                  AND tr.medical_record_id IS NOT NULL
                  AND tr.pay_status = 1 -- ✅ chỉ hiển thị khi đã thanh toán
                ORDER BY tr.id DESC
            """,
            countQuery = """
                        SELECT count(*) 
                        FROM test_reports tr
                        JOIN tests t ON tr.test_id = t.id
                        WHERE t.room_id = :roomId 
                          AND tr.medical_record_id IS NOT NULL
                          AND tr.pay_status = 1
                    """,
            nativeQuery = true)
    Page<TestRequestDto> findPendingReportsByRoom(@Param("roomId") Long roomId, Pageable pageable);


    @Transactional
    @Modifying
    @Query(value = """
                UPDATE test_reports
                SET pay_status = 1
                WHERE medical_record_id = :medicalRecordId
            """, nativeQuery = true)
    void markTestsAsPaidByMedicalRecord(@Param("medicalRecordId") Long medicalRecordId);
}
