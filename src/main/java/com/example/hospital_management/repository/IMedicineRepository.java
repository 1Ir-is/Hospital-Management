package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMedicineRepository extends JpaRepository<Medicine, Long> {
}
