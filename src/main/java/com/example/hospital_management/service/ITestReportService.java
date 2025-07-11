package com.example.hospital_management.service;

import com.example.hospital_management.entity.TestReport;

import java.util.List;

public interface ITestReportService {
    void save(TestReport testReport);

    List<TestReport> findByMedicalRecordId(Long id);
}
