package com.example.hospital_management.service;

import com.example.hospital_management.dto.BillingSummaryDto;
import com.example.hospital_management.dto.ImpatientRecordDto;

import java.util.List;

public interface IImpatientRecordService {
    List<ImpatientRecordDto> findAllUnpaidImpatients();
    BillingSummaryDto getBillingSummary(Long medicalRecordId);
    void markAsPaid(Long medicalRecordId);
}
