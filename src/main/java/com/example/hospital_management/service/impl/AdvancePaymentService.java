package com.example.hospital_management.service.impl;

import com.example.hospital_management.repository.IAdvancePaymentRepository;
import com.example.hospital_management.service.IAdvancePaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdvancePaymentService implements IAdvancePaymentService {
    private final IAdvancePaymentRepository advancePaymentRepository;
    @Autowired
    public AdvancePaymentService(IAdvancePaymentRepository repository) {
        this.advancePaymentRepository = repository;
    }
}
