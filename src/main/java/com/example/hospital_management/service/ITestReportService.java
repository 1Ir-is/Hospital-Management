package com.example.hospital_management.service;

import com.example.hospital_management.entity.TestReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ITestReportService {
    List<TestReport> findAll();
    Page<TestReport> findAll(Pageable pageable);

    void save(TestReport testReport);

    Optional<TestReport> findById(Long id);

    void remove(Long id);
    Page<TestReport> searchByName(String searchByName,Long medicalRecordId ,Pageable pageable);
}
