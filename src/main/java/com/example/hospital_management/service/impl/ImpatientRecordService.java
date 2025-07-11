package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.repository.IImpatientRecordRepository;
import com.example.hospital_management.service.IImpatientRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImpatientRecordService implements IImpatientRecordService {
   @Autowired
  private   IImpatientRecordRepository impatientRecordRepository;

    @Override
    public List<ImpatientRecord> findAllDangNhapVien() {
        return impatientRecordRepository.findAllDangNhapVien();
    }

    @Override
    public Optional<ImpatientRecord> findById(Long id) {
        return impatientRecordRepository.findById(id);
    }

    @Override
    public Page<ImpatientRecord> searchByFields(String patientName, String roomNumber, String doctorName, String nurseName, Pageable pageable) {
        return impatientRecordRepository.searchByFields(patientName, roomNumber, doctorName ,nurseName, pageable);
    }
    @Override
    public void updateNote(Long recordId, String note) {
        ImpatientRecord record = impatientRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ nội trú"));
        record.setNote(note);
        impatientRecordRepository.save(record);
    }


}
