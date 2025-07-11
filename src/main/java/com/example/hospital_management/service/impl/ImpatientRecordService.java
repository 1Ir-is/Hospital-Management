package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.repository.IImpatientRecordRepository;
import com.example.hospital_management.service.IImpatientRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import java.util.List;

@Service
public class ImpatientRecordService implements IImpatientRecordService {
    private IImpatientRecordRepository impatientRecordRepository;
    @Autowired
    public ImpatientRecordService(IImpatientRecordRepository impatientRecordRepository) {
        this.impatientRecordRepository = impatientRecordRepository;
    }

    @Override
    public Page<ImpatientRecord> findAll(Pageable pageable) {
        return impatientRecordRepository.findAll(pageable);
    }

    @Override
    public Optional<ImpatientRecord> findById(Long id) {
        return impatientRecordRepository.findById(id);
    }

    @Override
    public void remove(Long id) {
impatientRecordRepository.deleteById(id);
    }

    @Override
    public Page<ImpatientRecord> searchByName(String searchByName, Pageable pageable) {
        return impatientRecordRepository.searchByName(searchByName,pageable);
    }

    @Override
    public Page<ImpatientRecord> findAll(String patientName, Long employeeId, Pageable pageable) {
        return impatientRecordRepository.findRecordsByPatientNameAndEmployeeId(patientName,employeeId,pageable);
    }

    @Override
    public List<ImpatientRecord> findAll() {
        return impatientRecordRepository.findAll();
    }

    @Override
    public ImpatientRecord checkExistBedInRoom(Integer number) {
        return impatientRecordRepository.checkExistBedInRoom(number);
    }

    @Override
    public void save(ImpatientRecord impatientRecord) {
        impatientRecordRepository.save(impatientRecord);
    }

    @Override
    public Page<ImpatientRecord> findAllWaitingToImpatient(String patientName,String code, Pageable pageable) {
        return impatientRecordRepository.getListImpatientRecords(patientName,code, pageable);
    }

    @Override
    public ImpatientRecord getImpatientRecordById(Long id) {
        return impatientRecordRepository.findImpatientRecordById(id);
    }

    @Override
    public Page<ImpatientRecord> findAll(String patientName, Pageable pageable) {
        return impatientRecordRepository.findAllImpatientRecords(patientName,pageable);
    }
}
