package com.example.hospital_management.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="inpatient_treatments")
public class InpatientTreatment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer morningDose;
    private Integer eveningDose;
    private boolean status;
    private String note;
    private String treatmentLocation;
    private Integer estimateNumberOfDate;

    @ManyToOne
    @JoinColumn(name="impatient_record_id")
    private ImpatientRecord impatientRecord;

    @OneToOne
    @JoinColumn(name="medicine_id")
    private Medicine medicine;


}
