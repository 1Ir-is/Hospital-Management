package com.example.hospital_management.dto;

import com.example.hospital_management.entity.Medicine;
import com.example.hospital_management.entity.Prescription;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDetailDto {
    private String dosage;
    private String usageInstruction;
    private Integer duration;
    private Integer quantity;
    private Medicine medicine;
}
