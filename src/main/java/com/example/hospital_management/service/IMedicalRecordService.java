package com.example.hospital_management.service;

import com.example.hospital_management.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IMedicalRecordService {
    void save(MedicalRecord medicalRecord);
    Long findMaxId();
    Page<MedicalRecord> findAllWithOutVitalSign(Pageable pageable);
    MedicalRecord findById(Long id);
//    MedicalRecord findRoomByCode(String code);
}
