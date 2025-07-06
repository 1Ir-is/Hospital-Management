package com.example.hospital_management.repository;

import com.example.hospital_management.entity.VitalSign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IVitalSignRepository extends JpaRepository<VitalSign, Long> {
}
