package com.example.hospital_management.repository;

import com.example.hospital_management.dto.TestRequestDto;
import com.example.hospital_management.entity.TestReport;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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
                    mr.code AS medicalRecordCode,
                    t.name AS name,
                    tr.note AS note,
                    ts.name AS status
                FROM test_reports tr
                JOIN tests t ON tr.test_id = t.id
                JOIN medical_records mr ON tr.medical_record_id = mr.id
                JOIN patients p ON mr.patient_id = p.id
                JOIN test_status ts ON tr.test_status_id = ts.id
                WHERE tr.medical_record_id IS NOT NULL
                  AND tr.test_status_id != 2
                  AND tr.pay_status = 1
                  AND NOT EXISTS (
                      SELECT 1
                      FROM impatient_records ir
                      WHERE ir.medical_record_id = mr.id
                  )
                ORDER BY tr.id DESC
            """, nativeQuery = true)
    Page<TestRequestDto> findPendingReportsByRoom(@Param("roomId") Long roomId, Pageable pageable);


    @Query(value = "SELECT \n" +
            "r.id AS id,\n" +
            "p.name AS patientName,\n" +
            "p.id AS patientId,\n" +
            "mr.code AS medicalRecordCode,\n" +   // ✅ Thêm dòng này
            "te.name AS name,\n" +
            "r.note AS note,\n" +
            "ts.name AS status\n" +
            "FROM test_reports r\n" +
            "JOIN tests te ON r.test_id = te.id\n" +
            "JOIN medical_records mr ON r.medical_record_id = mr.id\n" +
            "JOIN patients p ON mr.patient_id = p.id\n" +
            "JOIN test_status ts ON r.test_status_id = ts.id\n" +
            "WHERE p.id = :patientId\n" +
            "ORDER BY r.id DESC", nativeQuery = true)
    List<TestRequestDto> findAllByPatientId(@Param("patientId") Long patientId);


    @Transactional
    @Modifying
    @Query(value = """
                UPDATE test_reports
                SET pay_status = 1
                WHERE medical_record_id = :medicalRecordId
            """, nativeQuery = true)
    void markTestsAsPaidByMedicalRecord(@Param("medicalRecordId") Long medicalRecordId);

    //    @Query(value = "SELECT * FROM test_reports WHERE title LIKE CONCAT('%', :searchName, '%')", nativeQuery = true)
//    Page<TestReport> searchByName(@Param("searchName") String searchName, Pageable pageable);
    @Query("SELECT tr FROM TestReport tr " +
            "WHERE (:searchName IS NULL OR LOWER(tr.medicalRecord.patient.name) LIKE LOWER(CONCAT('%', :searchName, '%'))) " +
            "AND (:medicalRecordId IS NULL OR tr.medicalRecord.id = :medicalRecordId)")
    Page<TestReport> searchByName(@Param("searchName") String searchName,
                                  @Param("medicalRecordId") Long medicalRecordId,
                                  Pageable pageable);

    @Query(value = """
            SELECT 
                tr.id AS id,
                p.name AS patientName,
                p.id AS patientId,
                mr.code AS medicalRecordCode,
                t.name AS name,
                tr.note AS note,
                ts.name AS status
            FROM test_reports tr
            JOIN tests t ON tr.test_id = t.id
            JOIN medical_records mr ON tr.medical_record_id = mr.id
            JOIN patients p ON mr.patient_id = p.id
            JOIN test_status ts ON tr.test_status_id = ts.id
            WHERE t.room_id = :roomId
              AND tr.test_status_id = 2
              AND DATE(tr.date) = CURDATE()
            ORDER BY tr.id DESC
            """, nativeQuery = true)
    List<TestRequestDto> findTodayCompletedReports(@Param("roomId") Long roomId);

    @Query("SELECT r.imagePath FROM TestReport r WHERE r.id = :id")
    String findImageByTestReportId(@Param("id") Long id);

}
