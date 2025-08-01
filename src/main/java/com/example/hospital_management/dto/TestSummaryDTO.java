package com.example.hospital_management.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestSummaryDTO {
    private Long inpatientRecordId;
    private Long totalOfTest;
    private Long completedTest;
    private String patientName;
    private Long patientId;
}
