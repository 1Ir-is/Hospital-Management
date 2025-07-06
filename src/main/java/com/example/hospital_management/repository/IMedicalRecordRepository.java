package com.example.hospital_management.repository;

import com.example.hospital_management.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
}
