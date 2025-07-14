package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Insurance;
import com.example.hospital_management.repository.IInsuranceRepository;
import com.example.hospital_management.service.IInsuranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InsuranceService implements IInsuranceService {
    private final IInsuranceRepository insuranceRepository;

    @Autowired
    public InsuranceService(IInsuranceRepository insuranceRepository) {
        this.insuranceRepository = insuranceRepository;
    }

    @Override
    public void save(Insurance insurance) {
        insuranceRepository.save(insurance);
    }

    @Override
    public boolean existsByCode(String code) {
        return insuranceRepository.existsByCode(code);
    }

    @Override
    public Insurance findByCode(String code) {
        return insuranceRepository.findByCode(code);
    }
}
