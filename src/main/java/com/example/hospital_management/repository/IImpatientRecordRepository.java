package com.example.hospital_management.repository;

import com.example.hospital_management.entity.AdvancePayment;
import com.example.hospital_management.entity.ImpatientRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IImpatientRecordRepository extends JpaRepository<ImpatientRecord, Long> {
    @Query("SELECT r FROM ImpatientRecord r WHERE r.status = true ")
    List<ImpatientRecord> findAllDangNhapVien();
    @Query("""
SELECT DISTINCT r FROM ImpatientRecord r
JOIN r.medicalRecord m
JOIN m.patient p
JOIN m.room ro

LEFT JOIN EmployeeAssigment eaDoctor ON eaDoctor.impatientRecord = r AND eaDoctor.employee.position.name = 'Bác sĩ'
LEFT JOIN EmployeeAssigment eaNurse ON eaNurse.impatientRecord = r AND eaNurse.employee.position.name = 'Điều dưỡng'

WHERE (:patientName IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :patientName, '%')))
  AND (:roomNumber IS NULL OR LOWER(CAST(ro.number AS string)) LIKE LOWER(CONCAT('%', :roomNumber, '%')))
  AND (:doctorName IS NULL OR :doctorName = '' OR EXISTS (
    SELECT 1 FROM EmployeeAssigment ed
    WHERE ed.impatientRecord = r
      AND ed.employee.position.name = 'Bác sĩ'
      AND LOWER(ed.employee.name) LIKE LOWER(CONCAT('%', :doctorName, '%'))
  ))
  AND (:nurseName IS NULL OR :nurseName = '' OR EXISTS (
    SELECT 1 FROM EmployeeAssigment en
    WHERE en.impatientRecord = r
      AND en.employee.position.name = 'Điều dưỡng'
      AND LOWER(en.employee.name) LIKE LOWER(CONCAT('%', :nurseName, '%'))
  ))
""")
    Page<ImpatientRecord> searchByFields(
            @Param("patientName") String patientName,
            @Param("roomNumber") String roomNumber,
            @Param("doctorName") String doctorName,
            @Param("nurseName") String nurseName,
            Pageable pageable
    );










}
