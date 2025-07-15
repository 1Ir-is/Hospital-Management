package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.ClinicalExamination;
import com.example.hospital_management.repository.IClinicalExaminationRepository;
import com.example.hospital_management.service.IClinicalExaminationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ClinicalExaminationServiceImpl implements IClinicalExaminationService {
    @Autowired
    private IClinicalExaminationRepository clinicalExaminationRepository;
    @Override
    public List<ClinicalExamination> getByImpatientRecordId(Long recordId) {

        return clinicalExaminationRepository.findByImpatientRecordIdOrderByDateDesc(recordId);
    }

    @Override
    public List<ClinicalExamination> findAll() {
        return clinicalExaminationRepository.findAll();
    }

    @Override
    public void save(ClinicalExamination impatientRecord) {
        clinicalExaminationRepository.save(impatientRecord);
    }

    @Override
    public List<ClinicalExamination> findByImpatientRecordId(Long id) {
        return clinicalExaminationRepository.findAllByImpatientRecordId(id);
    }
}
