package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Medicine;
import com.example.hospital_management.repository.IMedicineRepository;
import com.example.hospital_management.service.IMedicineService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicineService implements IMedicineService {
    public final IMedicineRepository iMedicineRepository;

    public MedicineService(IMedicineRepository iMedicineRepository) {
        this.iMedicineRepository = iMedicineRepository;
    }

    @Override
    public List<Medicine> getAll() {
        return iMedicineRepository.findAll();
    }
}
