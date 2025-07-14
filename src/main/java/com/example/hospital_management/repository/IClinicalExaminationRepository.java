package com.example.hospital_management.repository;

import com.example.hospital_management.entity.ClinicalExamination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IClinicalExaminationRepository extends JpaRepository<ClinicalExamination,Long> {
    List<ClinicalExamination> findAllByImpatientRecordId(Long id);
}
