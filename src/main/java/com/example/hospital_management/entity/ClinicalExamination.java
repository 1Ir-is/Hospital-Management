package com.example.hospital_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "clinical_examination")
public class ClinicalExamination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private String note;
    private String statusMedicine;

    @ManyToOne
    @JoinColumn(name = "impatient_record_id")
    private ImpatientRecord impatientRecord;

    @ManyToOne
    @JoinColumn(name = "vital_sign_id")
    private VitalSign vitalSign;
}
