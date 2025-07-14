package com.example.hospital_management.service.impl;

import com.example.hospital_management.dto.BillingSummaryDto;
import com.example.hospital_management.dto.ImpatientBasicDto;
import com.example.hospital_management.dto.ImpatientRecordDto;
import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.repository.IImpatientRecordRepository;
import com.example.hospital_management.repository.IMedicalRecordRepository;
import com.example.hospital_management.repository.IPrescriptionRepository;
import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.repository.IImpatientRecordRepository;
import com.example.hospital_management.service.IImpatientRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import java.util.List;
import java.util.Optional;

import java.util.List;

@Service
public class ImpatientRecordService implements IImpatientRecordService {

    private final IImpatientRecordRepository impatientRecordRepository;
    private final IMedicalRecordRepository medicalRecordRepository;
    private final IPrescriptionRepository prescriptionRepository;
    private final InsuranceService insuranceService;

    public ImpatientRecordService(IImpatientRecordRepository impatientRecordRepository,
                                  IMedicalRecordRepository medicalRecordRepository,
                                  IPrescriptionRepository prescriptionRepository, InsuranceService insuranceService) {
        this.impatientRecordRepository = impatientRecordRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.insuranceService = insuranceService;
    }

    @Override
    public Page<ImpatientBasicDto> findAllUnpaidImpatients(Pageable pageable) {
        return impatientRecordRepository.findAllUnpaidImpatients(pageable);
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
        Long totalFee = medicalFee + testFee + medicineFee;
        Long remaining = totalFee - advance;

        Long insuranceAmount = 0L;
        Long patientId = mr.getPatient().getId();
        if (insuranceService.existsValidInsurance(patientId, LocalDate.now())) {
            insuranceAmount = totalFee / 2; // giảm 50%
        }
        return new BillingSummaryDto(
                mr.getId(),
                mr.getPatient().getName(),
                mr.getCode(),
                medicalFee,
                testFee,
                medicineFee,
                totalFee,
                insuranceAmount,
                advance,
                remaining,
                true // ✅ là bệnh nhân nội trú        // Là nội trú
        );
    }

    @Override
    public void markAsPaid(Long medicalRecordId) {
        medicalRecordRepository.updatePaymentStatus(medicalRecordId);
        impatientRecordRepository.markTestsAsPaid(medicalRecordId);
        prescriptionRepository.markPrescriptionsAsPaidByMedicalRecord(medicalRecordId);
    }

    @Override
    public Page<ImpatientRecord> findAll(Pageable pageable) {
        return impatientRecordRepository.findAll(pageable);
    }

    @Override
    public Optional<ImpatientRecord> findById(Long id) {
        return impatientRecordRepository.findById(id);
    }

    @Override
    public void remove(Long id) {
        impatientRecordRepository.deleteById(id);
    }

    @Override
    public Page<ImpatientRecord> searchByName(String searchByName, Pageable pageable) {
        return impatientRecordRepository.searchByName(searchByName, pageable);
    }

    @Override
    public Page<ImpatientRecord> findAll(String patientName, Long employeeId, Pageable pageable) {
        return impatientRecordRepository.findRecordsByPatientNameAndEmployeeId(patientName, employeeId, pageable);
    }

    @Override
    public List<ImpatientRecord> findAll() {
        return impatientRecordRepository.findAll();
    }

    @Override
    public ImpatientRecord checkExistBedInRoom(Integer number) {
        return impatientRecordRepository.checkExistBedInRoom(number);
    }

    @Override
    public void save(ImpatientRecord impatientRecord) {
        impatientRecordRepository.save(impatientRecord);
    }

    @Override
    public Page<ImpatientRecord> findAllWaitingToImpatient(String patientName, String code, Pageable pageable) {
        return impatientRecordRepository.getListImpatientRecords(patientName, code, pageable);
    }

    @Override
    public ImpatientRecord getImpatientRecordById(Long id) {
        return impatientRecordRepository.findImpatientRecordById(id);
    }

    @Override
    public Page<ImpatientRecord> findAll(String patientName, Pageable pageable) {
        return impatientRecordRepository.findAllImpatientRecords(patientName, pageable);
    }
}
