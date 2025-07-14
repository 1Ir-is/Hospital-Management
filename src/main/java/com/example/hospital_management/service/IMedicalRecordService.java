package com.example.hospital_management.service;


import com.example.hospital_management.dto.BillingSummaryDto;
import com.example.hospital_management.dto.MedicalRecordBasicDto;

import java.util.List;


import com.example.hospital_management.dto.MedicalRecordDto;
import com.example.hospital_management.dto.TestSummaryDto;
import com.example.hospital_management.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMedicalRecordService {

    BillingSummaryDto getBillingSummary(Long medicalRecordId);

    List<MedicalRecordBasicDto> findAllBasicInfo();

    void markAsPaid(Long medicalRecordId);
    Long findMaxId();
    Page<MedicalRecord> findAllWithOutVitalSign(Pageable pageable);
    MedicalRecord findById(Long id);

//    MedicalRecord findRoomByCode(String code);
    Page<MedicalRecordDto> getWaitingRecords(Pageable pageable);

    MedicalRecordDto getCurrentPatient();

//    Optional<MedicalRecord> findById(Long id);

    void save(MedicalRecord medicalRecord);

    Page<TestSummaryDto> getTestingMedicalRecordList(Pageable pageable, Long roomId);

    MedicalRecordDto getCurrentPatient(Long roomId);

    Page<MedicalRecordDto> getWaitingRecords(Pageable pageable, Long roomId);


    List<MedicalRecord> findAll();
    MedicalRecord getMedicalRecordById(Long id);

    Page<MedicalRecordDto> getAllStatusRecords(Pageable pageable, Long roomId);
}
