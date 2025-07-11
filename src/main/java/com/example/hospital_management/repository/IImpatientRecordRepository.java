package com.example.hospital_management.repository;

import com.example.hospital_management.entity.ImpatientRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface IImpatientRecordRepository extends JpaRepository<ImpatientRecord, Long> {
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
