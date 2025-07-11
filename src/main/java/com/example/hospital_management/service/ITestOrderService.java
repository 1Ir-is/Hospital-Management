package com.example.hospital_management.service;


import com.example.hospital_management.dto.TestRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ITestOrderService {
    Page<TestRequestDto> findPendingOrdersNative(Long roomId, Pageable pageable);

    List<TestRequestDto> findAllByPatientId(Long patientId);
}

