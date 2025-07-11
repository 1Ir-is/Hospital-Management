package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Medicine;
import com.example.hospital_management.repository.IMedicineRepository;
import com.example.hospital_management.service.IMedicineService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicineService implements IMedicineService {
    private final IMedicineRepository repository;

    public MedicineService(IMedicineRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Medicine> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Medicine> findById(Long medicineId) {
        return repository.findById(medicineId);
    }
}
