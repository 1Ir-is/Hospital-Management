package com.example.hospital_management.service.impl;

import com.example.hospital_management.dto.PrescriptionRequestDto;
import com.example.hospital_management.entity.Prescription;
import com.example.hospital_management.repository.IPrescriptionRepository;
import com.example.hospital_management.entity.Prescription;
import com.example.hospital_management.repository.IPrescriptionRepository;
import com.example.hospital_management.service.IPrescriptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrescriptionService implements IPrescriptionService {

    private final IPrescriptionRepository prescriptionRepository;

    public PrescriptionService(IPrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public Page<PrescriptionRequestDto> getAllPrescriptions(Pageable pageable) {
        return prescriptionRepository.getAllPrescriptions(pageable);
    }

    @Override
    public Optional<Prescription> findById(Long id) {
        return prescriptionRepository.findById(id);
    }

    @Override
    public Prescription save(Prescription prescription) {
        return prescriptionRepository.save(prescription);
    }

    @Override
    public Optional<PrescriptionRequestDto> findFirstUnprocessedPrescription() {
        return prescriptionRepository.findFirstUnprocessedPrescription();
    }

    @Override
    public Prescription saveAndFlush(Prescription prescription) {
        return saveAndFlush(prescription);
    }

}
