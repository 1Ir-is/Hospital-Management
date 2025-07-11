package com.example.hospital_management.service.impl;

import com.example.hospital_management.dto.BillingSummaryDto;
import com.example.hospital_management.dto.ImpatientRecordDto;
import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.repository.IImpatientRecordRepository;
import com.example.hospital_management.repository.IMedicalRecordRepository;
import com.example.hospital_management.repository.IPrescriptionRepository;
import com.example.hospital_management.service.IImpatientRecordService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImpatientRecordService implements IImpatientRecordService {

    private final IImpatientRecordRepository impatientRecordRepository;
    private final IMedicalRecordRepository medicalRecordRepository;
    private final IPrescriptionRepository prescriptionRepository;

    public ImpatientRecordService(IImpatientRecordRepository impatientRecordRepository,
                                  IMedicalRecordRepository medicalRecordRepository,
                                  IPrescriptionRepository prescriptionRepository) {
        this.impatientRecordRepository = impatientRecordRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public List<ImpatientRecordDto> findAllUnpaidImpatients() {
        return impatientRecordRepository.findAllUnpaidImpatients();
    }

    @Override
    public BillingSummaryDto getBillingSummary(Long medicalRecordId) {
        MedicalRecord mr = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ khám"));

        Long medicineFee = impatientRecordRepository.getTotalMedicineFee(medicalRecordId);
        Long testFee = impatientRecordRepository.getTotalTestFee(medicalRecordId);
        Long advance = impatientRecordRepository.getAdvancePayment(medicalRecordId);

        medicineFee = (medicineFee == null) ? 0 : medicineFee;
        testFee = (testFee == null) ? 0 : testFee;
        advance = (advance == null) ? 0 : advance;

        Long medicalFee = (mr.getFee() == null) ? 0 : mr.getFee();
        Long totalFee = medicalFee + testFee + medicineFee ;
        Long remaining = totalFee - advance;

        return new BillingSummaryDto(
                mr.getId(),
                mr.getPatient().getName(),
                mr.getCode(),
                medicalFee,
                testFee,
                medicineFee,
                totalFee,
                advance,
                remaining,
                true               // Là nội trú
        );
    }

    @Override
    public void markAsPaid(Long medicalRecordId) {
        medicalRecordRepository.updatePaymentStatus(medicalRecordId);
        impatientRecordRepository.markTestsAsPaid(medicalRecordId);
        prescriptionRepository.markPrescriptionsAsPaidByMedicalRecord(medicalRecordId);
    }
}
