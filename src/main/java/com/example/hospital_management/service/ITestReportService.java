package com.example.hospital_management.service;

import com.example.hospital_management.dto.TestRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.example.hospital_management.entity.TestReport;

import java.util.List;

public interface ITestReportService {
    Page<TestRequestDto> findPendingReportsByRoom(@Param("roomId") Long roomId, Pageable pageable);

    void save(TestReport testReport);

    List<TestReport> findByMedicalRecordId(Long id);
}
