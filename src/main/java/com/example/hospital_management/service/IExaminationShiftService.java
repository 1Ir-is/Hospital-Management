package com.example.hospital_management.service;

import com.example.hospital_management.entity.ExaminationShift;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface IExaminationShiftService {
    List<ExaminationShift> findAll();
    List<ExaminationShift> findAllByRoom_Id(Long id);
    ExaminationShift findById(Long id);
    void save(ExaminationShift examinationShift);
}
