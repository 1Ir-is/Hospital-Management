package com.example.hospital_management.service;

import com.example.hospital_management.entity.Medicine;

import java.util.List;
import java.util.Optional;

public interface IMedicineService {
    List<Medicine> findAll();
    Optional<Medicine> findById(Long medicineId);
    List<Medicine> getAll();
}
