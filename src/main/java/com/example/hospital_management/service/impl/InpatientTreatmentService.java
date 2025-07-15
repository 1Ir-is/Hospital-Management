package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.InpatientTreatment;
import com.example.hospital_management.entity.TreatmentTask;
import com.example.hospital_management.repository.IInpatientTreatmentRepository;
import com.example.hospital_management.repository.ITreatmentTaskRepository;
import com.example.hospital_management.service.IInpatientTreatmentService;
import com.example.hospital_management.service.ITreatmentTaskService;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InpatientTreatmentService implements IInpatientTreatmentService {

    private final IInpatientTreatmentRepository iInpatientTreatmentRepository;
    private final ITreatmentTaskRepository treatmentTaskRepository;

    public InpatientTreatmentService(IInpatientTreatmentRepository iInpatientTreatmentRepository, ITreatmentTaskRepository treatmentTaskRepository) {
        this.iInpatientTreatmentRepository = iInpatientTreatmentRepository;
        this.treatmentTaskRepository = treatmentTaskRepository;
    }

    @Override
    public void save(InpatientTreatment inpatientTreatment) {
        InpatientTreatment savedTreatment   = iInpatientTreatmentRepository.save(inpatientTreatment);
        List<TreatmentTask> treatmentTasks = generateTaskForTreatment(savedTreatment);
        treatmentTaskRepository.saveAll(treatmentTasks);

    }

    @Override
    public List<InpatientTreatment> findByImpatientRecordId(Long recordId) {
        return iInpatientTreatmentRepository.findAllByRecordId(recordId);
    }

    private List<TreatmentTask> generateTaskForTreatment(InpatientTreatment inpatientTreatment){
        List<TreatmentTask> treatmentTasks = new ArrayList<>();
        LocalDate startDate = inpatientTreatment.getStartDate();
        LocalDate endDate = inpatientTreatment.getEndDate();
        if(startDate==null||endDate==null||startDate.isAfter(endDate)){
            return treatmentTasks;
        }
        for(LocalDate date = startDate;!date.isEqual(endDate);date = date.plusDays(1)){
            TreatmentTask treatmentTask = new TreatmentTask();
            treatmentTask.setDate(date);
            treatmentTask.setMorningDose(inpatientTreatment.getMorningDose());
            treatmentTask.setEveningDose(inpatientTreatment.getEveningDose());
            treatmentTask.setMorningStatus(false);
            treatmentTask.setEveningStatus(false);
            treatmentTask.setMorningNote("");
            treatmentTask.setEveningNote("");
            treatmentTask.setInpatientTreatment(inpatientTreatment);
            treatmentTasks.add(treatmentTask);
        }
        return treatmentTasks;
    }
}
