package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.entity.Patient;
import com.example.hospital_management.repository.IMedicalRecordRepository;
import com.example.hospital_management.service.IMedicalRecordService;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordService implements IMedicalRecordService {
    private IMedicalRecordRepository medicalRecordRepository;

    public MedicalRecordService(IMedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }


    @Override
    public MedicalRecord findRoomByCode(String code) {
        return medicalRecordRepository.findRoomByCode(code).orElseThrow(()->new RuntimeException("Không tìm thấy hồ sơ khám"));
    }

    @Override
    public List<MedicalRecord> findAll() {
        return medicalRecordRepository.findAll();
    }

    @Override
    public List<MedicalRecord> findByPatient(Patient patient) {
        return medicalRecordRepository.findByPatient(patient);
    }

    @Override
    public Optional<MedicalRecord> findById(String code) {
        return medicalRecordRepository.findByCode(code);
    }
}
