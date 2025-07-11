package com.example.hospital_management.service;

import com.example.hospital_management.entity.Patient;

import com.example.hospital_management.entity.Patient;

import java.util.List;
import java.util.Optional;

public interface IPatientService {
    void save(Patient patient);
    List<Patient> findALl();
    List<Patient> findAllByIdCard(String idCard);
}
