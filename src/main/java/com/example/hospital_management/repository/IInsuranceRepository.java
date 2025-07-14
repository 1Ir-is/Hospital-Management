package com.example.hospital_management.repository;

import com.example.hospital_management.entity.AdvancePayment;
import com.example.hospital_management.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInsuranceRepository extends JpaRepository<Insurance, Long> {
    boolean existsByCode(String code);
    Insurance findByCode(String code);
}
