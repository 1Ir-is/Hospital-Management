package com.example.hospital_management.repository;

import com.example.hospital_management.entity.ExaminationShift;
import com.example.hospital_management.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface IExaminationShiftRepository extends JpaRepository<ExaminationShift, Long> {
    List<ExaminationShift> findAllByRoom_Id(Long id);

    ExaminationShift findByMedicalRecord(MedicalRecord medicalRecord);

    @Query("""
                SELECT es FROM ExaminationShift es
                WHERE es.medicalRecord IS NOT NULL
                  AND es.medicalRecord.visitDate = :today
            """)
    Page<ExaminationShift> findCurrentShifts(LocalDate today, Pageable pageable);


}
