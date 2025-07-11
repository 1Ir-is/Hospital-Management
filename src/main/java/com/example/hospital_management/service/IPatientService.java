package com.example.hospital_management.service;

import com.example.hospital_management.entity.Patient;

import java.util.List;

import com.example.hospital_management.entity.Patient;
import com.example.hospital_management.entity.TestReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import com.example.hospital_management.entity.Patient;

import java.util.List;

public interface IPatientService {
    List<Patient> findAll();
    Patient getPatientById(Long id);
    Page<Patient> findAll(Pageable pageable);

    void save(Patient patient);

    Optional<Patient> findById(Long id);

    void remove(Long id);
    Page<Patient> searchByName(String searchByName ,Pageable pageable);

}
