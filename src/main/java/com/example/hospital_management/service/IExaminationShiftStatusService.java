package com.example.hospital_management.service;

import com.example.hospital_management.entity.ExaminationShiftStatus;
import com.example.hospital_management.entity.MedicalRecord;

import java.util.List;

public interface IExaminationShiftStatusService {
    ExaminationShiftStatus findById(Long id);
    List<ExaminationShiftStatus> findAll();
}
