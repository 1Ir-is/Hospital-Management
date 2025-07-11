package com.example.hospital_management.repository;

import com.example.hospital_management.entity.TestOrder;
import com.example.hospital_management.entity.TestReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITestOrderRepository extends JpaRepository<TestOrder, Long> {
    @Query("SELECT tr FROM TestReport tr " +
            "WHERE (:searchName IS NULL OR LOWER(tr.medicalRecord.patient.name) LIKE LOWER(CONCAT('%', :searchName, '%'))) " +
            "AND (:medicalRecordId IS NULL OR tr.medicalRecord.id = :medicalRecordId)")
    Page<TestOrder> searchByName(@Param("searchName") String searchName,
                                  @Param("medicalRecordId") Long medicalRecordId,
                                  Pageable pageable);

    List<TestOrder> findAllByImpatientRecord_Id(Long id);

}
