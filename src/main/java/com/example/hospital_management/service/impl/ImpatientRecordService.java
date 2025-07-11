package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.repository.IImpatientRecordRepository;
import com.example.hospital_management.service.IImpatientRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImpatientRecordService implements IImpatientRecordService {
    private final IImpatientRecordRepository iImpatientRecordRepository;

    public ImpatientRecordService(IImpatientRecordRepository iImpatientRecordRepository) {
        this.iImpatientRecordRepository = iImpatientRecordRepository;
    }


    @Override
    public Page<ImpatientRecord> findAll(String patientName, Long employeeId, Pageable pageable) {
        return iImpatientRecordRepository.findRecordsByPatientNameAndEmployeeId(patientName,employeeId,pageable);
    }

    @Override
    public List<ImpatientRecord> findAll() {
        return iImpatientRecordRepository.findAll();
    }

    @Override
    public ImpatientRecord checkExistBedInRoom(Integer number) {
        return iImpatientRecordRepository.checkExistBedInRoom(number);
    }

    @Override
    public void save(ImpatientRecord impatientRecord) {
        iImpatientRecordRepository.save(impatientRecord);
    }

    @Override
    public Page<ImpatientRecord> findAllWaitingToImpatient(String patientName,String code, Pageable pageable) {
        return iImpatientRecordRepository.getListImpatientRecords(patientName,code, pageable);
    }

    @Override
    public ImpatientRecord getImpatientRecordById(Long id) {
        return iImpatientRecordRepository.findImpatientRecordById(id);
    }

    @Override
    public Page<ImpatientRecord> findAll(String patientName, Pageable pageable) {
        return iImpatientRecordRepository.findAllImpatientRecords(patientName,pageable);
    }
}
