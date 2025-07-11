package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Patient;
import com.example.hospital_management.repository.IPatientRepository;
import com.example.hospital_management.service.IPatientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService implements IPatientService {
    public final IPatientRepository iPatientRepository;

    public PatientService(IPatientRepository iPatientRepository) {
        this.iPatientRepository = iPatientRepository;
    }

    @Override
    public List<Patient> findAll() {
        return iPatientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        return iPatientRepository.findPatientById(id);
    }
}
