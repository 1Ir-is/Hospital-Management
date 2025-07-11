package com.example.hospital_management.repository;

import com.example.hospital_management.entity.ExaminationShift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IExaminationShiftRepository extends JpaRepository<ExaminationShift, Long> {
    List<ExaminationShift> findAllByRoom_Id(Long id);
}
