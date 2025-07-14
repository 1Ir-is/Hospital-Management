package com.example.hospital_management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillingSummaryDto {
    private Long medicalRecordId;
    private String patientName;
    private String medicalRecordCode;

    private Long medicalFee;
    private Long testFee;
    private Long medicineFee;
    private Long totalFee;
    private Long insuranceAmount;

    private Long advancePayment;
    private Long remainingAmount;

    private boolean inpatient;
}