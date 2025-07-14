package com.example.hospital_management.service;

import com.example.hospital_management.entity.ClinicalExamination;

import java.util.List;

public interface IClinicalExaminationService {
    List<ClinicalExamination> getByImpatientRecordId(Long recordId);
}
