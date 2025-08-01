package com.example.hospital_management.service;

import com.example.hospital_management.entity.ImpatientRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import com.example.hospital_management.dto.BillingSummaryDto;
import com.example.hospital_management.dto.ImpatientBasicDto;
import com.example.hospital_management.entity.ImpatientRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IImpatientRecordService {
    Page<ImpatientBasicDto> findAllUnpaidImpatients(Pageable pageable);
    List<ImpatientRecord> findAllDangNhapVien();

    Optional<ImpatientRecord> findById(Long id);

    Page<ImpatientRecord> searchByFieldsWithDepartment( String patientName, String roomNumber, String doctorName, String nurseName,Long departmentId, Pageable pageable);

    void updateNote(Long recordId, String note);

    BillingSummaryDto getBillingSummary(Long medicalRecordId);

    void markAsPaid(Long medicalRecordId);

    List<ImpatientRecord> findAll();

    Page<ImpatientRecord> findAll(Pageable pageable);


    void save(ImpatientRecord impatientRecord);

    void remove(Long id);
    Page<ImpatientRecord> findAllByStatusTrue(String name,Pageable pageable);


    Page<ImpatientRecord> searchByName(String searchByName, Pageable pageable);

    Page<ImpatientRecord> findAll(String patientName, Long employeeId, Pageable pageable);

    ImpatientRecord checkExistBedInRoom(Integer number);

    Page<ImpatientRecord> findAllWaitingToImpatient(String patientName, String code, Pageable pageable);

    ImpatientRecord getImpatientRecordById(Long id);

    Page<ImpatientRecord> findAll(String patientName, Pageable pageable);
    Page<ImpatientRecord> findAllImpatientRecordsList(String patientName,String code, Pageable pageable);
}
