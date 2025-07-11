package com.example.hospital_management.service;

import com.example.hospital_management.entity.MedicalRecord;

import java.util.List;


import com.example.hospital_management.entity.MedicalRecord;

public interface IMedicalRecordService {
    List<MedicalRecord> findAll();
    MedicalRecord getMedicalRecordById(Long id);
}
