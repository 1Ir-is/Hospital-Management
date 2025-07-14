package com.example.hospital_management.repository;

import com.example.hospital_management.entity.ExaminationShift;
import com.example.hospital_management.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IExaminationShiftRepository extends JpaRepository<ExaminationShift, Long> {
    List<ExaminationShift> findAllByRoom_Id(Long id);

    ExaminationShift findByMedicalRecord(MedicalRecord medicalRecord);
    @Query("SELECT es FROM ExaminationShift es JOIN FETCH es.room WHERE es.medicalRecord.id = :recordId")
    Optional<ExaminationShift> findByMedicalRecordIdWithRoom(@Param("recordId") Long recordId);


    @Query("""
                SELECT es FROM ExaminationShift es
                WHERE es.medicalRecord IS NOT NULL
                  AND es.medicalRecord.visitDate = :today
            """)
    Page<ExaminationShift> findCurrentShifts(LocalDate today, Pageable pageable);


    ExaminationShift findByMedicalRecordId(Long medicalRecordId);

    @Query(value = """
            SELECT es FROM ExaminationShift es
            JOIN FETCH es.medicalRecord mr
            JOIN FETCH es.examinationShiftStatus
            WHERE mr.visitDate = :today
            """,
            countQuery = """
                    SELECT COUNT(es) FROM ExaminationShift es
                    JOIN es.medicalRecord mr
                    WHERE mr.visitDate = :today
                    """
    )
    Page<ExaminationShift> findTodayRecordsWithStatus(@Param("today") LocalDate today, Pageable pageable);

    @Query(value = """
            SELECT es FROM ExaminationShift es
            JOIN FETCH es.medicalRecord mr
            JOIN FETCH es.examinationShiftStatus status
            WHERE mr.visitDate = :today AND status.id = :statusId
            """,
            countQuery = """
                    SELECT COUNT(es) FROM ExaminationShift es
                    JOIN es.medicalRecord mr
                    JOIN es.examinationShiftStatus status
                    WHERE mr.visitDate = :today AND status.id = :statusId
                    """
    )
    Page<ExaminationShift> findByVisitDateAndStatus(@Param("today") LocalDate today,
                                                    @Param("statusId") Long statusId, Pageable pageable);
}
