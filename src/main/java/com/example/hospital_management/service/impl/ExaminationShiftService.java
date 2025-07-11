package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.ExaminationShift;
import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.repository.IExaminationShiftRepository;
import com.example.hospital_management.service.IExaminationShiftService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExaminationShiftService implements IExaminationShiftService {
    private final IExaminationShiftRepository repository;

    public ExaminationShiftService(IExaminationShiftRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ExaminationShift> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ExaminationShift> findById(Long shiftId) {
        return repository.findById(shiftId);
    }

    @Override
    public void save(ExaminationShift shift) {
        repository.save(shift);
    }

    @Override
    public ExaminationShift findByMedicalRecord(MedicalRecord medicalRecord) {
        return repository.findByMedicalRecord(medicalRecord);
    }
}
