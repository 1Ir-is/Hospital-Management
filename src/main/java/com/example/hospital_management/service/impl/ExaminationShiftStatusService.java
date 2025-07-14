package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.ExaminationShiftStatus;
import com.example.hospital_management.repository.IExaminationShiftRepository;
import com.example.hospital_management.repository.IExaminationShiftStatusRepository;
import com.example.hospital_management.service.IExaminationShiftStatusService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExaminationShiftStatusService implements IExaminationShiftStatusService {
    private final IExaminationShiftStatusRepository examinationShiftStatusRepository;

    public ExaminationShiftStatusService(IExaminationShiftStatusRepository examinationShiftStatusRepository) {
        this.examinationShiftStatusRepository = examinationShiftStatusRepository;
    }

    @Override
    public ExaminationShiftStatus findById(Long id) {
        return examinationShiftStatusRepository.findById(id).orElse(null);
    }

    @Override
    public List<ExaminationShiftStatus> findAll() {
        return examinationShiftStatusRepository.findAll();
    }
}
