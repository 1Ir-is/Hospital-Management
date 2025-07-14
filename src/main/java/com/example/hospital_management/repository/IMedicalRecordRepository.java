package com.example.hospital_management.repository;


import com.example.hospital_management.dto.MedicalRecordBasicDto;
import com.example.hospital_management.dto.MedicalRecordDto;
import com.example.hospital_management.dto.TestSummaryDto;
import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.entity.Patient;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IMedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    MedicalRecord getMedicalRecordById(Long id);

    @Query(value = "select * from medical_records mr where status = 0 order by queue_number asc", nativeQuery = true)
    List<MedicalRecord> getCurrentQueuePatient();

    @Query(value = """
            SELECT new com.example.hospital_management.dto.MedicalRecordDto(
                mr.id, mr.code, mr.queueNumber, mr.visitDate, mr.symptom, mr.status,
                p.name, r.name, 
                vs.bloodPressure, vs.weight, vs.height, vs.heartRate,
                p.id,
                p.birthday, p.gender,
                es.id
            )
            FROM ExaminationShift es
            JOIN es.medicalRecord mr
            JOIN mr.patient p
            JOIN es.room r
            JOIN mr.vitalSign vs
            WHERE mr.status = false
            ORDER BY mr.queueNumber
            """, countQuery = """
            SELECT count (*)
            FROM ExaminationShift es
            JOIN es.medicalRecord mr
            JOIN mr.patient p
            JOIN es.room r
            JOIN mr.vitalSign vs
            WHERE mr.status = false
            ORDER BY mr.queueNumber
            """)
    Page<MedicalRecordDto> getWaitingRecords(Pageable pageable);

    @Query("""
            SELECT new com.example.hospital_management.dto.MedicalRecordDto(
                mr.id, mr.code, mr.queueNumber, mr.visitDate, mr.symptom, mr.status,
                p.name, r.name, 
                vs.bloodPressure, vs.weight, vs.height, vs.heartRate,
                p.id, p.birthday, p.gender, es.id
            )
            FROM ExaminationShift es
            JOIN es.medicalRecord mr
            JOIN mr.patient p
            JOIN es.room r
            JOIN mr.vitalSign vs
            WHERE mr.status = false
            ORDER BY mr.queueNumber
            LIMIT 1
            """)
    MedicalRecordDto getCurrentRecord();

    @Query(value = """
            SELECT new com.example.hospital_management.dto.TestSummaryDto(
                mr.id,
                COUNT(tr.id),
                SUM(CASE WHEN tr.status = true THEN 1 ELSE 0 END),
                p.name,
                p.id
            )
            FROM TestReport tr
            JOIN tr.medicalRecord mr
            JOIN mr.patient p
            WHERE mr.conclusion is null 
            GROUP BY mr.id, p.id, p.name
            """, countQuery = """
                SELECT COUNT(DISTINCT mr.id)
                    FROM TestReport tr
                    JOIN tr.medicalRecord mr
                    JOIN mr.patient p
                    WHERE mr.conclusion is null
            """)
    Page<TestSummaryDto> getTestingMedicalRecordList(Pageable pageable);

    @Query(value = """
            SELECT new com.example.hospital_management.dto.MedicalRecordDto(
                mr.id, mr.code, mr.queueNumber, mr.visitDate, mr.symptom, mr.status,
                p.name, r.name, 
                vs.bloodPressure, vs.weight, vs.height, vs.heartRate,
                p.id,
                p.birthday, p.gender, es.id
            )
            FROM ExaminationShift es
            JOIN es.medicalRecord mr
            JOIN mr.patient p
            JOIN es.room r
            JOIN mr.vitalSign vs
            WHERE mr.status = false
            AND r.id = :roomId
            ORDER BY mr.queueNumber
            """, countQuery = """
            SELECT count (*)
            FROM ExaminationShift es
            JOIN es.medicalRecord mr
            JOIN mr.patient p
            JOIN es.room r
            JOIN mr.vitalSign vs
            WHERE mr.status = false
            AND r.id = :roomId
            ORDER BY mr.queueNumber
            """)
    Page<MedicalRecordDto> getWaitingRecords(Pageable pageable, @Param("roomId") Long shiftId);

    @Query("""
            SELECT new com.example.hospital_management.dto.MedicalRecordDto(
                mr.id, mr.code, mr.queueNumber, mr.visitDate, mr.symptom, mr.status,
                p.name, r.name, 
                vs.bloodPressure, vs.weight, vs.height, vs.heartRate,
                p.id, p.birthday, p.gender, es.id
            )
            FROM ExaminationShift es
            JOIN es.medicalRecord mr
            JOIN mr.patient p
            JOIN es.room r
            JOIN mr.vitalSign vs
            WHERE mr.status = false
            AND r.id = :roomId
            ORDER BY mr.queueNumber
            LIMIT 1
            """)
    MedicalRecordDto getCurrentRecord(@Param("roomId") Long roomId);

    @Query(value = """
            SELECT new com.example.hospital_management.dto.TestSummaryDto(
                mr.id,
                COUNT(tr.id),
                SUM(CASE WHEN tr.status = true THEN 1 ELSE 0 END),
                p.name,
                p.id
            )
            FROM ExaminationShift es
            JOIN es.medicalRecord mr
            JOIN mr.patient p
            JOIN TestReport tr on tr.medicalRecord = mr
            WHERE mr.conclusion is null 
            AND es.room.id = :roomId
            GROUP BY mr.id, p.id, p.name
            """, countQuery = """
                SELECT COUNT(DISTINCT mr.id)
                    FROM ExaminationShift es
                    JOIN es.medicalRecord mr
                    JOIN mr.patient p
                    JOIN TestReport tr on tr.medicalRecord = mr
                    WHERE mr.conclusion is null 
                    AND es.room.id = :roomId
            """)
    Page<TestSummaryDto> getTestingMedicalRecordList(Pageable pageable, @Param("room_id") Long room_id);

    //Để khi xóa tránh trùng hơn, nếu dùng count xóa sẽ trùng
    @Query("select COALESCE(MAX(m.id), 0) from MedicalRecord m")
    Long findMaxId();

    @Query("select m from MedicalRecord m where m.vitalSign is null ")
    Page<MedicalRecord> findAllWithOutVitalSign(Pageable pageable);

    @Query(value = "SELECT mr FROM MedicalRecord mr WHERE mr.code = :code")
    Optional<MedicalRecord> findRoomByCode(@Param("code") String code);

    Optional<MedicalRecord> findDistinctByPatient_IdCard(String patientIdCard);


    List<MedicalRecord> findByPatient(Patient patient);

    Optional<MedicalRecord> findByCode(String code);

    @Query(value = """
                SELECT SUM(m.price * pd.quantity)
                FROM prescriptions pr
                JOIN prescription_details pd ON pr.id = pd.precription_id
                JOIN medicines m ON pd.medicine_id = m.id
                WHERE pr.medical_record_id = :medicalRecordId
            """, nativeQuery = true)
    Long getTotalMedicineFee(@Param("medicalRecordId") Long medicalRecordId);


    @Query(value = """
                SELECT SUM(t.price)
                FROM test_orders to2
                JOIN tests t ON to2.test_id = t.id
                JOIN impatient_records ir ON to2.impatient_record_id = ir.id
                WHERE ir.ho_so_kham_id = :medicalRecordId AND to2.pay_status = 1
            """, nativeQuery = true)
    Long getTotalTestFee(@Param("medicalRecordId") Long medicalRecordId);


    @Query(value = """
                SELECT SUM(fee)
                FROM advance_payments
                WHERE impatient_record_id IN (
                    SELECT id FROM impatient_records WHERE ho_so_kham_id = :medicalRecordId
                )
            """, nativeQuery = true)
    Long getAdvancePayment(@Param("medicalRecordId") Long medicalRecordId);


    @Query("""
                SELECT new com.example.hospital_management.dto.MedicalRecordBasicDto(
                    mr.id, mr.code, p.name
                )
                FROM MedicalRecord mr
                JOIN mr.patient p
                WHERE mr.paymentStatus = false
            """)
    List<MedicalRecordBasicDto> findAllBasicInfo();

    @Modifying
    @Transactional
    @Query(value = """
                UPDATE medical_records
                SET payment_status = 1
                WHERE id = :medicalRecordId
            """, nativeQuery = true)
    void updatePaymentStatus(@Param("medicalRecordId") Long medicalRecordId);


}
