package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.PrescriptionDetail;
import com.example.hospital_management.repository.IPrescriptionDetailRepository;
import com.example.hospital_management.service.IPrescriptionDetailService;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionDetailService implements IPrescriptionDetailService {
    private final IPrescriptionDetailRepository repository;

    public PrescriptionDetailService(IPrescriptionDetailRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(PrescriptionDetail detail) {
        repository.save(detail);
    }
}
