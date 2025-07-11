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
@Table(name="treatment_tasks")
public class TreatmentTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private Integer morningDose;
    private Integer eveningDose;
    private boolean status;

    @ManyToOne
    @JoinColumn(name="inpatient_treatment_id")
    private InpatientTreatment inpatientTreatment;

}
