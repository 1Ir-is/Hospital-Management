package com.example.hospital_management.repository;

import com.example.hospital_management.dto.IPrescriptionDetailDto;
import com.example.hospital_management.dto.PrescriptionDetailDto;
import com.example.hospital_management.entity.PrescriptionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface IPrescriptionDetailRepository extends JpaRepository<PrescriptionDetail, Long> {

    @Query(value = "  SELECT \n" +
            "    m.name AS medicine_name,\n" +
            "    pd.dosage AS dosage,\n" +
            "    pd.usage_instruction AS usage_instruction,\n" +
            "    pd.quantity AS quantity\n" +
            "FROM\n" +
            "    prescription_details pd\n" +
            "JOIN\n" +
            "    medicines m ON pd.medicine_id = m.id\n" +
            "WHERE\n" +
            "    pd.precription_id = :prescriptionId;", nativeQuery = true)
    List<IPrescriptionDetailDto> getDetailsByPrescriptionId(@Param("prescriptionId") Long prescriptionId);
}
