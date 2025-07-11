package com.example.hospital_management.service;

import com.example.hospital_management.entity.ExaminationShift;
import com.example.hospital_management.entity.MedicalRecord;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IExaminationShiftService {
    List<ExaminationShift> findAll();

    Optional<ExaminationShift> findById(Long shiftId);

    void save(ExaminationShift shift);

    ExaminationShift findByMedicalRecord(MedicalRecord medicalRecord);
}
