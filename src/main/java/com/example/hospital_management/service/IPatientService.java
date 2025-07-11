package com.example.hospital_management.service;

import com.example.hospital_management.entity.Patient;

import java.util.List;
import java.util.Optional;

public interface IPatientService {
    List<Patient> findALl();

    Patient save(Patient patient);

    List<Patient> findAllByIdCard(String idCard);
}
