package com.example.hospital_management.service;

import com.example.hospital_management.entity.MedicalRecord;

import java.util.List;

public interface IMedicalRecordService {
    List<MedicalRecord> findAll();
}
