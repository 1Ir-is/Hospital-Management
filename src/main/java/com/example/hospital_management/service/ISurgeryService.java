package com.example.hospital_management.service;

import com.example.hospital_management.entity.Surgery;
import com.example.hospital_management.entity.TestOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ISurgeryService {
    List<Surgery> findAll();
    Page<Surgery> findAll(Pageable pageable);

    void save(Surgery surgery);

    Optional<Surgery> findById(Long id);

    void remove(Long id);
    Page<Surgery> searchByName(String searchByName, Long medicalRecordId , Pageable pageable);
}
