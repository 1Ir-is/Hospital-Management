package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPrescriptionRepository extends JpaRepository<Prescription, Long> {
}
