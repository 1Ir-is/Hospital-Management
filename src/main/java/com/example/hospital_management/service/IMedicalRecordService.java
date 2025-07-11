package com.example.hospital_management.service;


import com.example.hospital_management.dto.BillingSummaryDto;
import com.example.hospital_management.dto.MedicalRecordBasicDto;

import java.util.List;

public interface IMedicalRecordService {

    BillingSummaryDto getBillingSummary(Long medicalRecordId);

    List<MedicalRecordBasicDto> findAllBasicInfo();

    void markAsPaid(Long medicalRecordId);
}
