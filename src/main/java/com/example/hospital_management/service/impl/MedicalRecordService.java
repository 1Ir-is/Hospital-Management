package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.repository.IMedicalRecordRepository;
import com.example.hospital_management.service.IMedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService implements IMedicalRecordService {
    private final IMedicalRecordRepository iMedicalRecordRepository;

    public MedicalRecordService(IMedicalRecordRepository iMedicalRecordRepository) {
        this.iMedicalRecordRepository = iMedicalRecordRepository;
    }

    @Override
    public MedicalRecord getMedicalRecordById(Long id) {
        return iMedicalRecordRepository.getMedicalRecordById(id);
    }

    @Override
    public List<MedicalRecord> findAll() {
        return iMedicalRecordRepository.findAll();
    }
}
