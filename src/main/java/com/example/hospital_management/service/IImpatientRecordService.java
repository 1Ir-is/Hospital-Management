package com.example.hospital_management.service;

import com.example.hospital_management.entity.ImpatientRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IImpatientRecordService {
     Page<ImpatientRecord> findAll(String patientName, Long employeeId, Pageable pageable);
     List<ImpatientRecord> findAll();
     ImpatientRecord checkExistBedInRoom(Integer number);
     void save(ImpatientRecord impatientRecord);
     Page<ImpatientRecord> findAllWaitingToImpatient(String patientName, String code, Pageable pageable);
     ImpatientRecord getImpatientRecordById(Long id);
     Page<ImpatientRecord> findAll(String patientName, Pageable pageable);
}
