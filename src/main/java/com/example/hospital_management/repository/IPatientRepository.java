package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPatientRepository extends JpaRepository<Patient, Long> {
    Patient findPatientById(Long id);
}
