package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.TreatmentTask;
import com.example.hospital_management.repository.ITreatmentTaskRepository;
import com.example.hospital_management.service.ITreatmentTaskService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TreatmentTaskService implements ITreatmentTaskService {
    private final ITreatmentTaskRepository treatmentTaskRepository;

    public TreatmentTaskService(ITreatmentTaskRepository treatmentTaskRepository) {
        this.treatmentTaskRepository = treatmentTaskRepository;
    }

    @Override
    public List<TreatmentTask> getAllTreatmentTaskByTreatmentTaskById(Long treatmentTaskId) {
        return treatmentTaskRepository.getAllTreatmentTaskByInpatientTreatmentId(treatmentTaskId);
    }

    @Override
    public void save(TreatmentTask treatmentTask) {
        treatmentTaskRepository.save(treatmentTask);
    }

    @Override
    public TreatmentTask findById(Long id) {
        return treatmentTaskRepository.findById(id).orElse(null);
    }
}
