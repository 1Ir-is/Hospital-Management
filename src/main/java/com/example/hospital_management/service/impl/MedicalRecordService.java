package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.repository.IMedicalRecordRepository;
import com.example.hospital_management.service.IMedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService implements IMedicalRecordService {
    private final IMedicalRecordRepository medicalRecordRepository;

    @Autowired
    public MedicalRecordService(IMedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public void save(MedicalRecord medicalRecord) {
        medicalRecordRepository.save(medicalRecord);
    }

    @Override
    public Long findMaxId() {
        return medicalRecordRepository.findMaxId();
    }

    @Override
    public Page<MedicalRecord> findAllWithOutVitalSign(Pageable pageable) {
        return medicalRecordRepository.findAllWithOutVitalSign(pageable);
    }

    @Override
    public MedicalRecord findById(Long id) {
        return medicalRecordRepository.findById(id).orElse(null);
    }
}
