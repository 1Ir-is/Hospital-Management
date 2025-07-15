package com.example.hospital_management.service;

import com.example.hospital_management.entity.TreatmentTask;

import java.util.List;

public interface ITreatmentTaskService {
    List<TreatmentTask> getAllTreatmentTaskByTreatmentTaskById(Long treatmentTaskId);
    void save(TreatmentTask treatmentTask);
    TreatmentTask findById(Long id);
}
