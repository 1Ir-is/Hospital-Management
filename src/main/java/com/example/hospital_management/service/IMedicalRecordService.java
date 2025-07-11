package com.example.hospital_management.service;

import com.example.hospital_management.entity.MedicalRecord;

public interface IMedicalRecordService {
    MedicalRecord getMedicalRecordById(Long id);
}
