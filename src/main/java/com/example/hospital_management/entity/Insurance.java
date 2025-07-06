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
@Table(name = "insurances")
public class Insurance { //Bảo hiểm y tế
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private String registeredClinic;

    //Foreign Key
    //Patient - Bệnh nhân
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}
