package com.example.hospital_management.repository;

import com.example.hospital_management.entity.ExaminationShift;
import com.example.hospital_management.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface IExaminationShiftRepository extends JpaRepository<ExaminationShift, Long> {
    List<ExaminationShift> findAllByRoom_Id(Long id);

    ExaminationShift findByMedicalRecord(MedicalRecord medicalRecord);
    @Query("SELECT es FROM ExaminationShift es JOIN FETCH es.room WHERE es.medicalRecord.id = :recordId")
    Optional<ExaminationShift> findByMedicalRecordIdWithRoom(@Param("recordId") Long recordId);

}
