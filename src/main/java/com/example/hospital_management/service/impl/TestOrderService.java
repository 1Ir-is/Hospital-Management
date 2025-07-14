package com.example.hospital_management.service.impl;

import com.example.hospital_management.dto.TestRequestDto;
import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.entity.TestOrder;
import com.example.hospital_management.entity.TestStatus;
import com.example.hospital_management.repository.ITestOrderRepository;
import com.example.hospital_management.repository.ITestStatusRepository;
import com.example.hospital_management.service.ITestOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
public class TestOrderService implements ITestOrderService {
    private final ITestOrderRepository testOrderRepository;
    private final ITestStatusRepository testStatusRepository;


    public TestOrderService(ITestOrderRepository testOrderRepository, ITestStatusRepository testStatusRepository) {
        this.testOrderRepository = testOrderRepository;
        this.testStatusRepository = testStatusRepository;
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

    @Override
    @Transactional
    public void saveTestResult(Long testOrderId, String imageUrl, String note) {
        TestOrder testOrder = testOrderRepository.findById(testOrderId).orElseThrow(() -> new RuntimeException("TestOrder Not Found"));
        testOrder.setImagePath(imageUrl);
        testOrder.setNote(note);
        testOrder.setStatus(true);
        testOrderRepository.save(testOrder);
    }

    @Override
    @Transactional
    public void updateTestOrderStatus(Long testOrderId, Long testStatusId) {
        TestOrder testOrder = testOrderRepository.findById(testOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy test order với ID: " + testOrderId));

        TestStatus testStatus = testStatusRepository.findById(testStatusId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy test status với ID: " + testStatusId));

        testOrder.setTestStatus(testStatus);
        testOrder.setDate(LocalDate.now());
        testOrderRepository.save(testOrder);
    }

    @Override
    public List<TestRequestDto> findTodayCompletedTests(Long roomId) {
        return testOrderRepository.findTodayCompletedTests(roomId);
    }

    @Override
    public void markOrdersAsPaidByOrderRecord(Long impatientRecordId) {
        testOrderRepository.markOrdersAsPaidByOrderRecord(impatientRecordId);
    }

    @Override
    public void saveTestResult(Long testOrderId, String imageUrl, String note, Long employeeId) {
        TestOrder testOrder = testOrderRepository.findById(testOrderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu chỉ định với ID: " + testOrderId));

        testOrder.setImagePath(imageUrl);
        testOrder.setNote(note);
        testOrder.setStatus(true); // đánh dấu đã hoàn tất

        // ✅ Gán mới kỹ thuật viên thực hiện (không thay đổi ID của entity đã có)
        Employee employee = new Employee();
        employee.setId(employeeId);
        testOrder.setEmployee(employee); // gán employee mới, đúng cách

        testOrderRepository.save(testOrder);
    }

    @Override
    public String findImageByTestOrderId(Long id) {
        return testOrderRepository.findImageByTestOrderId(id);
    }
}
