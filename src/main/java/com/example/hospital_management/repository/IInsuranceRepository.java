package com.example.hospital_management.repository;

import com.example.hospital_management.entity.AdvancePayment;
import com.example.hospital_management.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface IInsuranceRepository extends JpaRepository<Insurance, Long> {
    boolean existsByCode(String code);

    @Query("""
    SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END
    FROM Insurance i
    WHERE i.patient.id = :patientId
      AND :today BETWEEN i.effectiveDate AND i.expiryDate
""")
    boolean existsValidInsurance(@Param("patientId") Long patientId, @Param("today") LocalDate today);
}
