package com.example.hospital_management.repository;


import com.example.hospital_management.dto.PrescriptionRequestDto;
import com.example.hospital_management.entity.Prescription;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface IPrescriptionRepository extends JpaRepository<Prescription, Long> {
    @Query(value = """
            SELECT
            			pr.id AS id,
                        p.name AS patientsName,
                        mr.code AS medicalRecordCode,
                        pr.created_date AS createdDate,
                        pr.status AS status
                        FROM prescriptions pr
                        JOIN medical_records mr ON pr.medical_record_id = mr.id
                        JOIN patients p ON mr.patient_id = p.id
                        WHERE pr.status =0
                        AND pr.pay_status = 1 -- ✅ Chỉ lấy đơn đã thanh toán
                        ORDER BY pr.created_date DESC""",
            countQuery = """
                    SELECT COUNT(*)
                    FROM prescriptions pr
                    JOIN medical_records mr ON pr.medical_record_id = mr.id
                    JOIN patients p ON mr.patient_id = p.id
                    WHERE pr.status = 'Chưa Cấp'
                    AND pr.pay_status = 1 -- ✅ Chỉ lấy đơn đã thanh toán
                    ORDER BY pr.created_date DESC; \s""", nativeQuery = true)
    Page<PrescriptionRequestDto> getAllPrescriptions(Pageable pageable);


    @Query(value = """
                SELECT 
                    p.id AS id,
                    pat.name AS patientsName,
                    mr.code AS medicalRecordCode,
                    p.created_date AS createdDate,
                    p.status AS status
                FROM prescriptions p
                JOIN medical_records mr ON p.medical_record_id = mr.id
                JOIN patients pat ON mr.patient_id = pat.id
                WHERE p.status = 'Chưa Cấp'
                  AND p.pay_status = 1 -- ✅ Chỉ lấy đơn đã thanh toán
                ORDER BY p.created_date ASC
                LIMIT 1
            """, nativeQuery = true)
    Optional<PrescriptionRequestDto> findFirstUnprocessedPrescription();

    @Query(value = """
                SELECT 
                    p.id AS id,
                    pt.name AS patientsName,
                    mr.code AS medicalRecordCode,
                    p.created_date AS createdDate,
                    p.status AS status
                FROM prescriptions p
                JOIN medical_records mr ON p.medical_record_id = mr.id
                JOIN patients pt ON mr.patient_id = pt.id
                WHERE p.status = 'Đã Cấp'
                  AND DATE(p.created_date) = CURRENT_DATE
                ORDER BY p.created_date DESC
            """, nativeQuery = true)
    List<PrescriptionRequestDto> findTodayDispensedPrescriptions();


    @Transactional
    @Modifying
    @Query(value = """
                UPDATE prescriptions
                SET pay_status = 1
                WHERE medical_record_id = :medicalRecordId
            """, nativeQuery = true)
    void markPrescriptionsAsPaidByMedicalRecord(@Param("medicalRecordId") Long medicalRecordId);

    @Query(value = """
            SELECT 
                p.id AS id,
                pat.name AS patientsName,
                mr.code AS medicalRecordCode,
                p.created_date AS createdDate,
                p.status AS status
            FROM prescriptions p
            JOIN medical_records mr ON p.medical_record_id = mr.id
            JOIN patients pat ON mr.patient_id = pat.id
            WHERE p.pay_status = 1
              AND mr.code = :code
            ORDER BY p.created_date DESC
            """, nativeQuery = true)
    List<PrescriptionRequestDto> findAllPaidPrescriptionsByMedicalRecordCode(@Param("code") String code);

}
