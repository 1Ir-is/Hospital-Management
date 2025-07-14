package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.ClinicalExamination;
import com.example.hospital_management.repository.IClinicalExaminationRepository;
import com.example.hospital_management.service.IClinicalExaminationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicalExaminationService implements IClinicalExaminationService {
    private IClinicalExaminationRepository clinicalExaminationRepository;

    public ClinicalExaminationService(IClinicalExaminationRepository clinicalExaminationRepository) {
        this.clinicalExaminationRepository = clinicalExaminationRepository;
    }

    @Override
    public List<ClinicalExamination> findAll() {
        return clinicalExaminationRepository.findAll();
    }

    @Override
    public void save(ClinicalExamination clinicalExamination) {
        clinicalExaminationRepository.save(clinicalExamination);
    }

    @Override
    public List<ClinicalExamination> findByImpatientRecordId(Long id) {
        return clinicalExaminationRepository.findAllByImpatientRecordId(id);
    }
}
