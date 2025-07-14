package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.ExaminationShift;
import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.repository.IExaminationShiftRepository;
import com.example.hospital_management.service.IExaminationShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
    public Page<ExaminationShift> getCurrentShifts(int page, int size) {
        return examinationShiftRepository.findCurrentShifts(LocalDate.now(), PageRequest.of(page, size));
    }

    @Override
    public ExaminationShift findByMedicalRecord(MedicalRecord medicalRecord) {
        return examinationShiftRepository.findByMedicalRecord(medicalRecord);
    }
}
