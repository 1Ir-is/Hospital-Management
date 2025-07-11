package com.example.hospital_management.repository;

import com.example.hospital_management.dto.PrescriptionDetailDto;
import com.example.hospital_management.entity.PrescriptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface IPrescriptionDetailRepository extends JpaRepository<PrescriptionDetail, Long> {

    @Query(value = "SELECT m.name AS medicineName, " +
            "pd.dosage AS dosage, " +
            "pd.usage_instruction AS usageInstruction, " +
            "pd.quantity AS quantity " +
            "FROM prescription_details pd " +
            "JOIN medicines m ON pd.medicine_id = m.id " +
            "WHERE pd.precription_id = :prescriptionId", nativeQuery = true)
    List<PrescriptionDetailDto> getDetailsByPrescriptionId(Long prescriptionId);
}
