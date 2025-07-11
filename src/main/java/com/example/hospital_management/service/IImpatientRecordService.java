package com.example.hospital_management.service;

import com.example.hospital_management.entity.ImpatientRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IImpatientRecordService {
    List<ImpatientRecord> findAllDangNhapVien();

    Optional<ImpatientRecord> findById(Long id);

    Page<ImpatientRecord> searchByFields(String patientName, String roomNumber, String doctorName, String nurseName, Pageable pageable);



    void updateNote(Long recordId, String note);
}
