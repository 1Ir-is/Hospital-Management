package com.example.hospital_management.service;

import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.entity.Patient;

import java.util.List;
import java.util.Optional;

public interface IMedicalRecordService {
    MedicalRecord findRoomByCode(String code);
    List<MedicalRecord> findAll();

    List<MedicalRecord> findByPatient(Patient patient);

    Optional<MedicalRecord> findById(String code);
}
