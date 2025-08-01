package com.example.hospital_management.service;

import com.example.hospital_management.dto.PrescriptionRequestDto;
import com.example.hospital_management.entity.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import com.example.hospital_management.entity.Prescription;
import org.springframework.data.repository.query.Param;

public interface IPrescriptionService {
    Prescription save(Prescription prescription);

    Page<PrescriptionRequestDto> getAllPrescriptions(Pageable pageable);

    Optional<Prescription> findById(Long id);

    Optional<PrescriptionRequestDto> findFirstUnprocessedPrescription();

    Prescription saveAndFlush(Prescription prescription);

    List<PrescriptionRequestDto> findTodayDispensedPrescriptions();

    List<PrescriptionRequestDto> findAllPaidPrescriptionsByMedicalRecordCode(String code);
}
