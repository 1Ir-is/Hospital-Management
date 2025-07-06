package com.example.hospital_management.repository;

import com.example.hospital_management.entity.AdvancePayment;
import com.example.hospital_management.entity.ExaminationShiftStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExaminationShiftStatusRepository extends JpaRepository<ExaminationShiftStatus, Long> {
}
