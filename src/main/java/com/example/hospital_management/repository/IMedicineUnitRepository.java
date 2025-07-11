package com.example.hospital_management.repository;

import com.example.hospital_management.entity.MedicineUnit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMedicineUnitRepository extends JpaRepository<MedicineUnit, Long> {
}
