package com.example.hospital_management.service;

import com.example.hospital_management.entity.TestOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ITestOrderService {
    List<TestOrder> findAll();

    Page<TestOrder> findAll(Pageable pageable);

    void save(TestOrder testOrder);

    List<TestOrder> findByImpatientRecordId(Long id);

    TestOrder findById(Long id);

    void remove(Long id);

    Page<TestOrder> searchByName(String searchByName, Long medicalRecordId, Pageable pageable);

}
