package com.example.hospital_management.repository;

import com.example.hospital_management.entity.SurgeryType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISurgeryTypeRepository extends JpaRepository<SurgeryType, Long> {
}
