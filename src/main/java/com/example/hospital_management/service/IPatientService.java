package com.example.hospital_management.service;

import com.example.hospital_management.entity.Patient;

import java.util.List;

import com.example.hospital_management.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IPatientService {
    void save(Patient patient);

    List<Patient> findALl();

    List<Patient> findAllByIdCard(String idCard);

    List<Patient> findAll();

    Patient getPatientById(Long id);

    Page<Patient> findAll(Pageable pageable);

    Optional<Patient> findById(Long id);

    void remove(Long id);

    Page<Patient> searchByName(String searchByName, Pageable pageable);
}
