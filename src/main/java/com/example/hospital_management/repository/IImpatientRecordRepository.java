package com.example.hospital_management.repository;

import com.example.hospital_management.entity.AdvancePayment;
import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.entity.TestOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IImpatientRecordRepository extends JpaRepository<ImpatientRecord, Long> {
    @Query("SELECT ipt FROM ImpatientRecord ipt " +
            "WHERE (:searchName IS NULL OR LOWER(ipt.medicalRecord.patient.name) LIKE LOWER(CONCAT('%', :searchName, '%'))) ")
    Page<ImpatientRecord> searchByName(@Param("searchName") String searchName,
                                 Pageable pageable);
}
