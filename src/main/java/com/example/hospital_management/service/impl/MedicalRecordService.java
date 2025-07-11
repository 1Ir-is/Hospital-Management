package com.example.hospital_management.service.impl;

import com.example.hospital_management.dto.MedicalRecordDto;
import com.example.hospital_management.dto.TestSummaryDto;
import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.repository.IMedicalRecordRepository;
import com.example.hospital_management.service.IMedicalRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicalRecordService implements IMedicalRecordService {
    private final IMedicalRecordRepository repository;

    public MedicalRecordService(IMedicalRecordRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<MedicalRecordDto> getWaitingRecords(Pageable pageable) {
        return repository.getWaitingRecords(pageable);
    }

    @Override
    public MedicalRecordDto getCurrentPatient() {
        return repository.getCurrentRecord();
    }

    @Override
    public Optional<MedicalRecord> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void save(MedicalRecord medicalRecord) {
        repository.save(medicalRecord);
    }

    @Override
    public Page<TestSummaryDto> getTestingMedicalRecordList(Pageable pageable) {
        return repository.getTestingMedicalRecordList(pageable);
    }

    @Override
    public MedicalRecordDto getCurrentPatient(Long roomId) {
        return repository.getCurrentRecord(roomId);
    }

    @Override
    public Page<MedicalRecordDto> getWaitingRecords(Pageable pageable, Long roomId) {
        return repository.getWaitingRecords(pageable, roomId);
    }

}
