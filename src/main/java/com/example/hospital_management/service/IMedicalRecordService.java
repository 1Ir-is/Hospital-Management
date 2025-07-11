package com.example.hospital_management.service;

import com.example.hospital_management.dto.MedicalRecordDto;
import com.example.hospital_management.dto.TestSummaryDto;
import com.example.hospital_management.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


import com.example.hospital_management.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMedicalRecordService {
    Long findMaxId();
    Page<MedicalRecord> findAllWithOutVitalSign(Pageable pageable);
    /////////
    MedicalRecord findById(Long id);

//    MedicalRecord findRoomByCode(String code);
    Page<MedicalRecordDto> getWaitingRecords(Pageable pageable);

    MedicalRecordDto getCurrentPatient();

//    Optional<MedicalRecord> findById(Long id);

    void save(MedicalRecord medicalRecord);

    Page<TestSummaryDto> getTestingMedicalRecordList(Pageable pageable);

    MedicalRecordDto getCurrentPatient(Long roomId);

    Page<MedicalRecordDto> getWaitingRecords(Pageable pageable, Long roomId);


}
