package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.TestOrder;
import com.example.hospital_management.entity.TestReport;
import com.example.hospital_management.repository.ITestReportRepository;
import com.example.hospital_management.service.ITestReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestReportService implements ITestReportService {
    private ITestReportRepository testReportRepository;
    @Autowired
    public TestReportService(ITestReportRepository testReportRepository) {
        this.testReportRepository = testReportRepository;
    }


    @Override
    public List<TestReport> findAll() {
        return testReportRepository.findAll();
    }

    @Override
    public Page<TestReport> findAll(Pageable pageable) {
        return testReportRepository.findAll(pageable);
    }

    @Override
    public void save(TestReport testReport) {
        testReportRepository.save(testReport);
    }

    @Override
    public Optional<TestReport> findById(Long id) {
        return testReportRepository.findById(id);
    }

    @Override
    public void remove(Long id) {
        testReportRepository.deleteById(id);
    }

    @Override
    public Page<TestReport> searchByName(String searchByName,Long medicalRecordId, Pageable pageable) {
        return testReportRepository.searchByName(searchByName,medicalRecordId,pageable);
    }
}
