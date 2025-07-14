package com.example.hospital_management.service.impl;

import com.example.hospital_management.dto.TestRequestDto;
import com.example.hospital_management.entity.TestOrder;
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

    @Override
    public List<TestOrder> findAll() {
        return testOrderRepository.findAll();
    }

    @Override
    public Page<TestOrder> findAll(Pageable pageable) {
        return testOrderRepository.findAll(pageable);
    }

    @Override
    public void save(TestOrder testOrder) {
        testOrderRepository.save(testOrder);
    }

    @Override
    public List<TestOrder> findByImpatientRecordId(Long id) {
        return testOrderRepository.findAllByImpatientRecord_Id(id);
    }


    @Override
    public TestOrder findById(Long id) {
        return testOrderRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(Long id) {
        testOrderRepository.deleteById(id);
    }

    @Override
    public Page<TestOrder> searchByName(String searchByName, Long medicalRecordId, Pageable pageable) {
        return testOrderRepository.searchByName(searchByName, medicalRecordId, pageable);
    }
}
