package com.example.hospital_management.repository;

import com.example.hospital_management.dto.MedicalRecordDto;
import com.example.hospital_management.dto.TestSummaryDto;
import com.example.hospital_management.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
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
            """,
    countQuery = """
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
            """,
    countQuery = """
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
            """,
            countQuery = """
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
            """,
            countQuery = """
        SELECT COUNT(DISTINCT mr.id)
            FROM ExaminationShift es
            JOIN es.medicalRecord mr
            JOIN mr.patient p
            JOIN TestReport tr on tr.medicalRecord = mr
            WHERE mr.conclusion is null 
            AND es.room.id = :roomId
    """)
    Page<TestSummaryDto> getTestingMedicalRecordList(Pageable pageable, @Param("room_id") Long room_id);
}
