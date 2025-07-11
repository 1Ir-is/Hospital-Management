package com.example.hospital_management.service;

import com.example.hospital_management.entity.AdvancePayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAdvancePaymentService {
    Page<AdvancePayment> findAll(String patientName,Long employeeId, Pageable pageable);
    void save(AdvancePayment advancePayment);
}
