package com.example.hospital_management.service.impl;

import com.example.hospital_management.dto.BillingSummaryDto;
import com.example.hospital_management.dto.MedicalRecordBasicDto;
import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.repository.IMedicalRecordRepository;
import com.example.hospital_management.repository.IPrescriptionRepository;
import com.example.hospital_management.repository.ITestReportRepository;
import com.example.hospital_management.service.IMedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class MedicalRecordService implements IMedicalRecordService {
    private final IMedicalRecordRepository medicalRecordRepository;

    private final ITestReportRepository testReportRepository;

    private final IPrescriptionRepository prescriptionRepository;

    public MedicalRecordService(IMedicalRecordRepository medicalRecordRepository,
                                ITestReportRepository testReportRepository,
                                IPrescriptionRepository prescriptionRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.testReportRepository = testReportRepository;
        this.prescriptionRepository = prescriptionRepository;
    }

    @Override
    public BillingSummaryDto getBillingSummary(Long medicalRecordId) {
        MedicalRecord mr = medicalRecordRepository.findById(medicalRecordId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ khám"));

        Long medicineFee = medicalRecordRepository.getTotalMedicineFee(medicalRecordId);
        Long testFee = medicalRecordRepository.getTotalTestFee(medicalRecordId);
        Long advance = medicalRecordRepository.getAdvancePayment(medicalRecordId);

        medicineFee = (medicineFee == null) ? 0 : medicineFee;
        testFee = (testFee == null) ? 0 : testFee;
        advance = (advance == null) ? 0 : advance;

        Long medicalFee = (mr.getFee() == null) ? 0 : mr.getFee();
        Long totalFee = medicalFee + testFee + medicineFee;
        Long remaining = totalFee - advance;

        return new BillingSummaryDto(
                mr.getId(),
                mr.getPatient().getName(),
                mr.getCode(),
                medicalFee,
                testFee,
                medicineFee,
                totalFee,
                0L,
                totalFee,
                false
        );
    }

    @Override
    public List<MedicalRecordBasicDto> findAllBasicInfo() {
        return medicalRecordRepository.findAllBasicInfo();
    }

    @Override
    public void markAsPaid(Long medicalRecordId) {
        // ✅ Cập nhật trạng thái hồ sơ khám
        medicalRecordRepository.updatePaymentStatus(medicalRecordId);

        // ✅ Cập nhật trạng thái xét nghiệm
        testReportRepository.markTestsAsPaidByMedicalRecord(medicalRecordId);

        // ✅ Cập nhật trạng thái đơn thuốc
        prescriptionRepository.markPrescriptionsAsPaidByMedicalRecord(medicalRecordId);
    }
}
