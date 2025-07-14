package com.example.hospital_management.service;

import com.example.hospital_management.entity.Insurance;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface IInsuranceService {
    void save(Insurance insurance);
    boolean existsByCode(String code);

    boolean existsValidInsurance(@Param("patientId") Long patientId, @Param("today") LocalDate today);

}
