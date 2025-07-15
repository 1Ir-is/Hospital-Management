package com.example.hospital_management.repository;


import com.example.hospital_management.dto.TestRequestDto;
import com.example.hospital_management.entity.TestOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.hospital_management.entity.TestReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.List;

@Repository
public interface ITestOrderRepository extends JpaRepository<TestOrder, Long> {
    @Query(
            value = """
        SELECT 
            t.id AS id,
            p.name AS patientName,
            p.id AS patientId,
            mr.code AS medicalRecordCode,
            te.name AS name,
            t.note AS note,
            ts.name AS status
        FROM test_orders t
        JOIN tests te ON t.test_id = te.id
        JOIN impatient_records ir ON t.impatient_record_id = ir.id
        JOIN medical_records mr ON ir.medical_record_id = mr.id 
        JOIN patients p ON mr.patient_id = p.id
        JOIN test_status ts ON t.test_status_id = ts.id
        WHERE te.room_id = :roomId 
          AND t.test_status_id != 2
          AND t.pay_status = 1
        ORDER BY t.id DESC
    """,
            countQuery = """
        SELECT COUNT(*)
        FROM test_orders t
        JOIN tests te ON t.test_id = te.id
        JOIN impatient_records ir ON t.impatient_record_id = ir.id
        JOIN medical_records mr ON ir.medical_record_id = mr.id 
        JOIN patients p ON mr.patient_id = p.id
        JOIN test_status ts ON t.test_status_id = ts.id
        WHERE te.room_id = :roomId 
          AND t.test_status_id != 2
          AND t.pay_status = 1
    """,
            nativeQuery = true
    )
    Page<TestRequestDto> findPendingOrdersNative(@Param("roomId") Long roomId, Pageable pageable);


    @Query(value = "SELECT \n" +
            "    t.id AS id,\n" +
            "    p.name AS patientName,\n" +
            "    p.id AS patientId,\n" +
            "    mr.code AS medicalRecordCode,\n" +
            "    te.name AS name,\n" +
            "    t.note AS note,\n" +
            "    ts.name AS status\n" +
            "FROM test_orders t\n" +
            "JOIN tests te ON t.test_id = te.id\n" +
            "JOIN impatient_records ir ON t.impatient_record_id = ir.id\n" +
            "JOIN medical_records mr ON ir.medical_record_id = mr.id\n" +
            "JOIN patients p ON mr.patient_id = p.id\n" +
            "JOIN test_status ts ON t.test_status_id = ts.id\n" +
            "WHERE p.id = :patientId\n" +
            "ORDER BY t.id DESC", nativeQuery = true)
    List<TestRequestDto> findAllByPatientId(@Param("patientId") Long patientId);


    @Query("SELECT tr FROM TestReport tr " +
            "WHERE (:searchName IS NULL OR LOWER(tr.medicalRecord.patient.name) LIKE LOWER(CONCAT('%', :searchName, '%'))) " +
            "AND (:medicalRecordId IS NULL OR tr.medicalRecord.id = :medicalRecordId)")
    Page<TestOrder> searchByName(@Param("searchName") String searchName,
                                 @Param("medicalRecordId") Long medicalRecordId,
                                 Pageable pageable);

    List<TestOrder> findAllByImpatientRecord_Id(Long id);

    @Query(value = """
              SELECT\s
                  t.id AS id,
                  p.name AS patientName,
                  p.id AS patientId,
                  mr.code AS medicalRecordCode,        -- ✅ Thêm dòng này
                  te.name AS name,
                  t.note AS note,
                  ts.name AS status
              FROM test_orders t
              JOIN tests te ON t.test_id = te.id
              JOIN impatient_records ir ON t.impatient_record_id = ir.id
              JOIN medical_records mr ON ir.medical_record_id = mr.id
              JOIN patients p ON mr.patient_id = p.id
              JOIN test_status ts ON t.test_status_id = ts.id
              WHERE te.room_id = :roomId
                AND t.test_status_id = 2
                AND DATE(t.date) = CURDATE()
              ORDER BY t.id DESC;
            """,
            nativeQuery = true)
    List<TestRequestDto> findTodayCompletedTests(@Param("roomId") Long roomId);

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE test_orders
            SET pay_status = 1
            WHERE impatient_record_id = :impatientRecordId
        """, nativeQuery = true)
    void markOrdersAsPaidByOrderRecord(@Param("impatientRecordId") Long impatientRecordId);

    @Query("SELECT t.imagePath FROM TestOrder t WHERE t.id = :id")
    String findImageByTestOrderId(@Param("id") Long id);

}
