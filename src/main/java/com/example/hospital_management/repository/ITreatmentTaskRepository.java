package com.example.hospital_management.repository;

import com.example.hospital_management.entity.TreatmentTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITreatmentTaskRepository extends JpaRepository<TreatmentTask,Long> {
}
