package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Prescription;
import com.example.hospital_management.repository.IPrescriptionRepository;
import com.example.hospital_management.service.IPrescriptionService;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionService implements IPrescriptionService {
    private final IPrescriptionRepository repository;

    public PrescriptionService(IPrescriptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Prescription save(Prescription prescription) {
        return repository.save(prescription);
    }
}
