package com.example.hospital_management.service.impl;

import com.example.hospital_management.dto.TestRequestDto;
import com.example.hospital_management.repository.ITestOrderRepository;
import com.example.hospital_management.service.ITestOrderService;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestOrderService implements ITestOrderService {
    private final ITestOrderRepository testOrderRepository;

    public TestOrderService(ITestOrderRepository testOrderRepository) {
        this.testOrderRepository = testOrderRepository;
    }

    @Override
    public Page<TestRequestDto> findPendingOrdersNative(Long roomId, Pageable pageable) {
        return testOrderRepository.findPendingOrdersNative(roomId, pageable);
    }

    @Override
    public List<TestRequestDto> findAllByPatientId(Long patientId) {
        return testOrderRepository.findAllByPatientId(patientId);
    }
}
