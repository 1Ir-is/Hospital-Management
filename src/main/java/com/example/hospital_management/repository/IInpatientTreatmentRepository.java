package com.example.hospital_management.repository;

import com.example.hospital_management.entity.InpatientTreatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IInpatientTreatmentRepository extends JpaRepository<InpatientTreatment,Long> {
    @Query(value = "SELECT * FROM inpatient_treatments it WHERE it.impatient_record_id = :recordId ORDER BY it.start_date DESC", nativeQuery = true)
    List<InpatientTreatment> findAllByRecordId(@Param("recordId") Long recordId);
}
