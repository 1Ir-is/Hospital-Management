package com.example.hospital_management.repository;


import com.example.hospital_management.dto.TestRequestDto;
import com.example.hospital_management.entity.TestOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.hospital_management.entity.TestReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.List;

@Repository
public interface ITestOrderRepository extends JpaRepository<TestOrder, Long> {
    @Query(
            value = "SELECT \n" +
                    "t.id AS id,\n" +
                    "p.name AS patientName,\n" +
                    "p.id AS patientId,\n" +
                    "te.name AS name,\n" +
                    "t.note AS note,\n" +
                    "ts.name AS status\n" +
                    "FROM test_orders t\n" +
                    "JOIN tests te ON t.test_id = te.id\n" +
                    "JOIN impatient_records ir ON t.impatient_record_id = ir.id\n" +
                    "JOIN medical_records mr ON ir.ho_so_kham_id = mr.id\n" +
                    "JOIN patients p ON mr.patient_id = p.id\n" +
                    "JOIN test_status ts ON t.test_status_id = ts.id\n" +
                    "WHERE te.room_id = :roomId\n" +
                    "ORDER BY t.id DESC",
            countQuery = "SELECT \n" +
                    "count(*)\n" +
                    "FROM test_orders t\n" +
                    "JOIN tests te ON t.test_id = te.id\n" +
                    "JOIN impatient_records ir ON t.impatient_record_id = ir.id\n" +
                    "JOIN medical_records mr ON ir.ho_so_kham_id = mr.id\n" +
                    "JOIN patients p ON mr.patient_id = p.id\n" +
                    "WHERE te.room_id = :roomId",
            nativeQuery = true
    )
    Page<TestRequestDto> findPendingOrdersNative(@Param("roomId") Long roomId, Pageable pageable);

    @Query(value = "SELECT " +
            "t.id AS id, " +
            "p.name AS patientName, " +
            "p.id AS patientId, " +
            "te.name AS name, " +
            "t.note AS note, " +
            "ts.name AS status " +
            "FROM test_orders t " +
            "JOIN tests te ON t.test_id = te.id " +
            "JOIN impatient_records ir ON t.impatient_record_id = ir.id " +
            "JOIN medical_records mr ON ir.ho_so_kham_id = mr.id " +
            "JOIN patients p ON mr.patient_id = p.id " +
            "JOIN test_status ts ON t.test_status_id = ts.id " +
            "WHERE p.id = :patientId " +
            "ORDER BY t.id DESC", nativeQuery = true)
    List<TestRequestDto> findAllByPatientId(@Param("patientId") Long patientId);
    @Query("SELECT tr FROM TestReport tr " +
            "WHERE (:searchName IS NULL OR LOWER(tr.medicalRecord.patient.name) LIKE LOWER(CONCAT('%', :searchName, '%'))) " +
            "AND (:medicalRecordId IS NULL OR tr.medicalRecord.id = :medicalRecordId)")
    Page<TestOrder> searchByName(@Param("searchName") String searchName,
                                  @Param("medicalRecordId") Long medicalRecordId,
                                  Pageable pageable);

    List<TestOrder> findAllByImpatientRecord_Id(Long id);

}
