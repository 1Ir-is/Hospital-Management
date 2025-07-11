package com.example.hospital_management.service;

import com.example.hospital_management.dto.TestRequestDto;
import com.example.hospital_management.entity.TestReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ITestReportService {
    Page<TestRequestDto> findPendingReportsByRoom(@Param("roomId") Long roomId, Pageable pageable);

    void save(TestReport testReport);

    List<TestReport> findByMedicalRecordId(Long id);

    List<TestReport> findAll();

    Page<TestReport> findAll(Pageable pageable);

    Optional<TestReport> findById(Long id);

    void remove(Long id);

    Page<TestReport> searchByName(String searchByName, Long medicalRecordId, Pageable pageable);
}
