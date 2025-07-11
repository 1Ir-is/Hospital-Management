package com.example.hospital_management.service;

import com.example.hospital_management.entity.ImpatientRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IImpatientRecordService {
    List<ImpatientRecord> findAll();
    Page<ImpatientRecord> findAll(Pageable pageable);
    Optional<ImpatientRecord> findById(Long id);
    void save(ImpatientRecord impatientRecord);
    void remove(Long id);
    Page<ImpatientRecord> searchByName(String searchByName,  Pageable pageable);
}
