package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Patient;
import com.example.hospital_management.repository.IPatientRepository;
import com.example.hospital_management.service.IPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientService implements IPatientService {
    private final IPatientRepository patientRepository;

    @Autowired
    public PatientService(IPatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public void save(Patient patient) {
        patientRepository.save(patient);
    }
}
