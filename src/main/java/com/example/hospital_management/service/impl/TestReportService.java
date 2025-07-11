package com.example.hospital_management.service.impl;

import com.example.hospital_management.dto.TestRequestDto;
import com.example.hospital_management.repository.ITestReportRepository;
import com.example.hospital_management.entity.TestReport;
import com.example.hospital_management.repository.ITestReportRepository;
import com.example.hospital_management.service.ITestReportService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestReportService implements ITestReportService {
    private final ITestReportRepository testReportRepository;

    public TestReportService(ITestReportRepository testReportRepository) {
        this.testReportRepository = testReportRepository;
    }

    @Override
    public Page<TestRequestDto> findPendingReportsByRoom(Long roomId, Pageable pageable) {
        return testReportRepository.findPendingReportsByRoom(roomId, pageable);
    }

    @Override
    public void save(TestReport testReport) {
        testReportRepository.save(testReport);
    }

    @Override
    public List<TestReport> findByMedicalRecordId(Long id) {
        return testReportRepository.findByMedicalRecordId(id);
    }

}
