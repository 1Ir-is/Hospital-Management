package com.example.hospital_management.repository;

import com.example.hospital_management.entity.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IWorkScheduleRepository extends JpaRepository<WorkSchedule, Long> {
}
