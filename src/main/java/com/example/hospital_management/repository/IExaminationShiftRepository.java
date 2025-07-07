package com.example.hospital_management.repository;

import com.example.hospital_management.entity.AdvancePayment;
import com.example.hospital_management.entity.ExaminationShift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IExaminationShiftRepository extends JpaRepository<ExaminationShift, Long> {
}
