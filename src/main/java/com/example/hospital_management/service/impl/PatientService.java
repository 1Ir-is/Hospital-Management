package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Patient;
import com.example.hospital_management.repository.IPatientRepository;
import com.example.hospital_management.service.IPatientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService implements IPatientService {
    private IPatientRepository patientRepository;

    public PatientService(IPatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Patient> findALl() {
        return patientRepository.findAll();
    }

    @Override
    public Patient save(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> findAllByIdCard(String idCard) {
        return patientRepository.findAllByIdCard(idCard);
    }
}
