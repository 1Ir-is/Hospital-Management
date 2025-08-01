package com.example.hospital_management.service;


import com.example.hospital_management.dto.TestRequestDto;
import com.example.hospital_management.entity.TestOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITestOrderService {
    Page<TestRequestDto> findPendingOrdersNative(Long roomId, Pageable pageable);

    List<TestRequestDto> findAllByPatientId(Long patientId);

    List<TestOrder> findAll();

    Page<TestOrder> findAll(Pageable pageable);

    void save(TestOrder testOrder);

    List<TestOrder> findByImpatientRecordId(Long id);

    TestOrder findById(Long id);

    void remove(Long id);

    Page<TestOrder> searchByName(String searchByName, Long medicalRecordId, Pageable pageable);

    void saveTestResult(Long testOrderId, String imageUrl, String note);

    void updateTestOrderStatus(Long testOrderId, Long testStatusId);

    List<TestRequestDto> findTodayCompletedTests(Long roomId);

    void markOrdersAsPaidByOrderRecord(Long impatientRecordId);

    void saveTestResult(Long testOrderId, String imageUrl, String note, Long employeeId);

    String findImageByTestOrderId( Long id);

}

