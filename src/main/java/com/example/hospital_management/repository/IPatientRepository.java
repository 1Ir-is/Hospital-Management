package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IPatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findAllByIdCard(String idCard);
}
