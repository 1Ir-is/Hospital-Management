package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.InpatientTreatment;
import com.example.hospital_management.repository.IInpatientTreatmentRepository;
import com.example.hospital_management.service.IInpatientTreatmentService;
import org.springframework.stereotype.Service;

@Service
public class InpatientTreatmentService implements IInpatientTreatmentService {

    public final IInpatientTreatmentRepository iInpatientTreatmentRepository;

    public InpatientTreatmentService(IInpatientTreatmentRepository iInpatientTreatmentRepository) {
        this.iInpatientTreatmentRepository = iInpatientTreatmentRepository;
    }

    @Override
    public void save(InpatientTreatment inpatientTreatment) {
        iInpatientTreatmentRepository.save(inpatientTreatment);
    }
}
