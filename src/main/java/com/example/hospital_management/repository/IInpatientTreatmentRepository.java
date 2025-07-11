package com.example.hospital_management.repository;

import com.example.hospital_management.entity.InpatientTreatment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInpatientTreatmentRepository extends JpaRepository<InpatientTreatment,Long> {
}
