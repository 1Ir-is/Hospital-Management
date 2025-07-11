package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.AdvancePayment;
import com.example.hospital_management.repository.IAdvancePaymentRepository;
import com.example.hospital_management.service.IAdvancePaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdvancePaymentService implements IAdvancePaymentService {
    private final IAdvancePaymentRepository repository;

    public AdvancePaymentService(IAdvancePaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<AdvancePayment> findAll(String patientName,Long employeeId, Pageable pageable) {
        return repository.findAdvancePaymentByPatientNameAndEmployeeId(patientName,employeeId,pageable);
    }

    @Override
    public void save(AdvancePayment advancePayment) {
        repository.save(advancePayment);
    }
}
