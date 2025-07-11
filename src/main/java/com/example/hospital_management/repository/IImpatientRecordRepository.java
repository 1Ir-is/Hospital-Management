package com.example.hospital_management.repository;

import com.example.hospital_management.dto.ImpatientBasicDto;
import com.example.hospital_management.entity.ImpatientRecord;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface IImpatientRecordRepository extends JpaRepository<ImpatientRecord, Long> {

    @Query("""
                SELECT new com.example.hospital_management.dto.ImpatientBasicDto(
                    ir.id, mr.code, p.name
                )
                FROM ImpatientRecord ir
                JOIN ir.medicalRecord mr
                JOIN mr.patient p
                WHERE mr.paymentStatus = false
                ORDER BY ir.admissionDate DESC
            """)
    List<ImpatientBasicDto> findAllUnpaidImpatients();


    @Query(value = """
                SELECT SUM(t.price)
                FROM test_orders to2
                JOIN tests t ON to2.test_id = t.id
                JOIN impatient_records ir ON to2.impatient_record_id = ir.id
                WHERE ir.ho_so_kham_id = :medicalRecordId AND to2.pay_status = 1
            """, nativeQuery = true)
    Long getTotalTestFee(@Param("medicalRecordId") Long medicalRecordId);

    @Query(value = """
                SELECT SUM(m.price * pd.quantity)
                FROM prescriptions pr
                JOIN prescription_details pd ON pr.id = pd.precription_id
                JOIN medicines m ON pd.medicine_id = m.id
                WHERE pr.medical_record_id = :medicalRecordId
            """, nativeQuery = true)
    Long getTotalMedicineFee(@Param("medicalRecordId") Long medicalRecordId);

    @Query(value = """
                SELECT SUM(fee)
                FROM advance_payments
                WHERE impatient_record_id IN (
                    SELECT id FROM impatient_records WHERE ho_so_kham_id = :medicalRecordId
                )
            """, nativeQuery = true)
    Long getAdvancePayment(@Param("medicalRecordId") Long medicalRecordId);

    @Modifying
    @Transactional
    @Query(value = """
                UPDATE test_orders
                SET pay_status = 1
                WHERE impatient_record_id IN (
                    SELECT id FROM impatient_records WHERE ho_so_kham_id = :medicalRecordId
                )
            """, nativeQuery = true)
    void markTestsAsPaid(@Param("medicalRecordId") Long medicalRecordId);

    @Query("SELECT ipt FROM ImpatientRecord ipt " +
            "WHERE (:searchName IS NULL OR LOWER(ipt.medicalRecord.patient.name) LIKE LOWER(CONCAT('%', :searchName, '%'))) ")
    Page<ImpatientRecord> searchByName(@Param("searchName") String searchName,
                                       Pageable pageable);

    @Query(
            value = "SELECT ir.* " +
                    "FROM employee_assignments ea " +
                    "JOIN employees e ON ea.employee_id = e.id " +
                    "JOIN impatient_records ir ON ea.impatient_record_id = ir.id " +
                    "JOIN medical_records mr on ir.medical_record_id = mr.id " +
                    "JOIN patients p ON mr.patient_id = p.id " +
                    "WHERE p.name LIKE CONCAT('%', :patientName, '%') " +
                    "AND e.id = :employeeId",

            countQuery = "SELECT COUNT(*) " +
                    "FROM employee_assignments ea " +
                    "JOIN employees e ON ea.employee_id = e.id " +
                    "JOIN impatient_records ir ON ea.impatient_record_id = ir.id " +
                    "JOIN medical_records mr on ir.medical_record_id = mr.id " +
                    "JOIN patients p ON mr.patient_id = p.id " +
                    "WHERE p.name LIKE CONCAT('%', :patientName, '%') " +
                    "AND e.id = :employeeId",

            nativeQuery = true
    )
    Page<ImpatientRecord> findRecordsByPatientNameAndEmployeeId(
            @Param("patientName") String patientName,
            @Param("employeeId") Long employeeId,
            Pageable pageable
    );

    @Query(
            value = "SELECT ir.* " +
                    "FROM employee_assignments ea " +
                    "JOIN employees e ON ea.employee_id = e.id " +
                    "JOIN impatient_records ir ON ea.impatient_record_id = ir.id " +
                    "JOIN medical_records mr on ir.medical_record_id = mr.id " +
                    "JOIN patients p ON mr.patient_id = p.id " +
                    "WHERE p.name LIKE CONCAT('%', :patientName, '%') ",

            countQuery = "SELECT COUNT(*) " +
                    "FROM employee_assignments ea " +
                    "JOIN employees e ON ea.employee_id = e.id " +
                    "JOIN impatient_records ir ON ea.impatient_record_id = ir.id " +
                    "JOIN medical_records mr on ir.medical_record_id = mr.id " +
                    "JOIN patients p ON mr.patient_id = p.id " +
                    "WHERE p.name LIKE CONCAT('%', :patientName, '%') ",

            nativeQuery = true
    )
    Page<ImpatientRecord> findAllImpatientRecords(
            @Param("patientName") String patientName,
            Pageable pageable
    );


    @Query(value = "Select * from impatient_records ir " +
            "join beds b on ir.bed_id = b.id " +
            "join rooms r on r.id = b.room_id " +
            "where b.number = :number", nativeQuery = true)
    ImpatientRecord checkExistBedInRoom(@Param("number") Integer number);

    @Query(value = "select ir.* from impatient_records ir " +
            "join medical_records mr on ir.medical_record_id = mr.id " +
            "join patients p on mr.patient_id = p.id where ir.status = 0 and " +
            "p.name like concat ('%', :patientName , '%') " +
            "AND mr.code like concat ('%', :code, '%')",
            countQuery = "select count(*) from impatient_records ir " +
                    "join medical_records mr on ir.medical_record_id = mr.id " +
                    "join patients p on mr.patient_id = p.id where ir.status = 0 and " +
                    "p.name like concat ('%', :patientName ,'%') " +
                    "AND mr.code like concat ('%', :code, '%')", nativeQuery = true)
    Page<ImpatientRecord> getListImpatientRecords(@Param("patientName") String patientName,
                                                  @Param("code") String code,
                                                  Pageable pageable);

    ImpatientRecord findImpatientRecordById(Long id);
}
