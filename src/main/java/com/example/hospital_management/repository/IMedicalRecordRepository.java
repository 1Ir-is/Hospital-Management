package com.example.hospital_management.repository;


import com.example.hospital_management.dto.MedicalRecordBasicDto;
import com.example.hospital_management.entity.MedicalRecord;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    @Query(value = """
                SELECT SUM(m.price * pd.quantity)
                FROM prescriptions pr
                JOIN prescription_details pd ON pr.id = pd.precription_id
                JOIN medicines m ON pd.medicine_id = m.id
                WHERE pr.medical_record_id = :medicalRecordId
            """, nativeQuery = true)
    Long getTotalMedicineFee(@Param("medicalRecordId") Long medicalRecordId);


    @Query(value = """
                SELECT SUM(t.price)
                FROM test_orders to2
                JOIN tests t ON to2.test_id = t.id
                JOIN impatient_records ir ON to2.impatient_record_id = ir.id
                WHERE ir.ho_so_kham_id = :medicalRecordId AND to2.pay_status = 1
            """, nativeQuery = true)
    Long getTotalTestFee(@Param("medicalRecordId") Long medicalRecordId);


    @Query(value = """
                SELECT SUM(fee)
                FROM advance_payments
                WHERE impatient_record_id IN (
                    SELECT id FROM impatient_records WHERE ho_so_kham_id = :medicalRecordId
                )
            """, nativeQuery = true)
    Long getAdvancePayment(@Param("medicalRecordId") Long medicalRecordId);


    @Query("""
                SELECT new com.example.hospital_management.dto.MedicalRecordBasicDto(
                    mr.id, mr.code, p.name
                )
                FROM MedicalRecord mr
                JOIN mr.patient p
                WHERE mr.paymentStatus = false
            """)
    List<MedicalRecordBasicDto> findAllBasicInfo();

    @Modifying
    @Transactional
    @Query(value = """
                UPDATE medical_records
                SET payment_status = 1
                WHERE id = :medicalRecordId
            """, nativeQuery = true)
    void updatePaymentStatus(@Param("medicalRecordId") Long medicalRecordId);
}
