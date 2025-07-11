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
@Table(name = "impatient_records")
public class ImpatientRecord { // Hồ sơ nhập viện
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate admissionDate; //Ngày nhập viện
    private LocalDate dischargeDate; //Ngày xuất viện
    private Boolean status;
    private String note;
    private String reason;
    @Transient
    private Employee assignedDoctor;
    @Transient
    private Employee assignedNurse;


    //Foreign Key
    //Room - phòng

    //Bed - giường
    @ManyToOne
    @JoinColumn(name = "bed_id")
    private Bed bed;

    //medical - thuốc
    @OneToOne
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

}
