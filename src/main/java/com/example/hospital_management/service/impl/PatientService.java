package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Patient;
import com.example.hospital_management.repository.IPatientRepository;
import com.example.hospital_management.service.IPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Patient> findALl() {
        return patientRepository.findAll();
    }

    @Override
    public List<Patient> findAllByIdCard(String idCard) {
        return patientRepository.findAllByIdCard(idCard);
    }

    @Override
    public Page<Patient> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    @Override
    public void remove(Long id) {
        patientRepository.deleteById(id);
    }

    @Override
    public Page<Patient> searchByName(String searchByName, Pageable pageable) {
        return patientRepository.searchByName(searchByName, pageable);
    }

    @Override
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findPatientById(id);
    }
}
