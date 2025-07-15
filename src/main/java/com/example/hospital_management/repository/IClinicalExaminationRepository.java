package com.example.hospital_management.repository;

import com.example.hospital_management.entity.ClinicalExamination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IClinicalExaminationRepository extends JpaRepository<ClinicalExamination, Long> {

    List<ClinicalExamination> findByImpatientRecordIdOrderByDateDesc(Long recordId);

    List<ClinicalExamination> findAllByImpatientRecordId(Long id);
}
