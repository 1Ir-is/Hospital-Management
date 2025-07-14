package com.example.hospital_management.service;

import com.example.hospital_management.entity.ExaminationShift;
import com.example.hospital_management.entity.MedicalRecord;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IExaminationShiftService {

    //    Optional<ExaminationShift> findById(Long shiftId);
    ExaminationShift findByMedicalRecord(MedicalRecord medicalRecord);

    List<ExaminationShift> findAll();

    List<ExaminationShift> findAllByRoom_Id(Long id);

    ExaminationShift findById(Long id);

    void save(ExaminationShift examinationShift);

    Page<ExaminationShift> getCurrentShifts(int page, int size);
}
