package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.ExaminationShift;
import com.example.hospital_management.repository.IExaminationShiftRepository;
import com.example.hospital_management.entity.ExaminationShift;
import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.repository.IExaminationShiftRepository;
import com.example.hospital_management.service.IExaminationShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExaminationShiftService implements IExaminationShiftService {
    private final IExaminationShiftRepository examinationShiftRepository;

    @Autowired
    public ExaminationShiftService(IExaminationShiftRepository examinationShiftRepository) {
        this.examinationShiftRepository = examinationShiftRepository;
    }

    @Override
    public List<ExaminationShift> findAll() {
        return examinationShiftRepository.findAll();
    }

    @Override
    public List<ExaminationShift> findAllByRoom_Id(Long id) {
        return examinationShiftRepository.findAllByRoom_Id(id);
    }

    @Override
    public ExaminationShift findById(Long id) {
        return examinationShiftRepository.findById(id).orElse(null);
    }

//    @Override
//    public Optional<ExaminationShift> findById(Long shiftId) {
//        return examinationShiftRepository.findById(shiftId);
//    }

    @Override
    public void save(ExaminationShift examinationShift) {
        examinationShiftRepository.save(examinationShift);
    }

    @Override
    public ExaminationShift findByMedicalRecord(MedicalRecord medicalRecord) {
        return examinationShiftRepository.findByMedicalRecord(medicalRecord);
    }
}
