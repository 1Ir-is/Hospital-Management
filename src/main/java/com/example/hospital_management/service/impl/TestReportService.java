package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.TestReport;
import com.example.hospital_management.repository.ITestReportRepository;
import com.example.hospital_management.service.ITestReportService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestReportService implements ITestReportService {
    private final ITestReportRepository repository;

    public TestReportService(ITestReportRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(TestReport testReport) {
        repository.save(testReport);
    }

    @Override
    public List<TestReport> findByMedicalRecordId(Long id) {
        return repository.findByMedicalRecordId(id);
    }
}
