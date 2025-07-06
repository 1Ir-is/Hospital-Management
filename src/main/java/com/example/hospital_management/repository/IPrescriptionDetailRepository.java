package com.example.hospital_management.repository;

import com.example.hospital_management.entity.PrescriptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPrescriptionDetailRepository extends JpaRepository<PrescriptionDetail, Long> {
}
