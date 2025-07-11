package com.example.hospital_management.service;

import com.example.hospital_management.dto.PrescriptionRequestDto;
import com.example.hospital_management.entity.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IPrescriptionService {
    Page<PrescriptionRequestDto> getAllPrescriptions(Pageable pageable);

    Optional<Prescription> findById(Long id);

    void save(Prescription prescription);

    Optional<PrescriptionRequestDto> findFirstUnprocessedPrescription();
}
