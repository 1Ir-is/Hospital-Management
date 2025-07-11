package com.example.hospital_management.dto;

import com.example.hospital_management.entity.MedicalRecord;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDto {
    private Long id;
    private LocalDate createdDate;
    private String note;
    private MedicalRecord medicalRecord;
    private List<PrescriptionDetailDto> prescriptionDetails;
}
