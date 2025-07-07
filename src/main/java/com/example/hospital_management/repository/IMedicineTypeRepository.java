package com.example.hospital_management.repository;

import com.example.hospital_management.entity.MedicineType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMedicineTypeRepository extends JpaRepository<MedicineType, Long> {
}
