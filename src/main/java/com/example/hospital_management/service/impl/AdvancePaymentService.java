package com.example.hospital_management.service.impl;

import com.example.hospital_management.repository.IAdvancePaymentRepository;
import com.example.hospital_management.service.IAdvancePaymentService;
import org.springframework.stereotype.Service;

@Service
public class AdvancePaymentService implements IAdvancePaymentService {
    private final IAdvancePaymentRepository repository;

    public AdvancePaymentService(IAdvancePaymentRepository repository) {
        this.repository = repository;
    }
}
