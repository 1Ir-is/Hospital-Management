package com.example.hospital_management.service;

import com.example.hospital_management.dto.TestRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface ITestReportService {
    Page<TestRequestDto> findPendingReportsByRoom(@Param("roomId") Long roomId, Pageable pageable);

}
