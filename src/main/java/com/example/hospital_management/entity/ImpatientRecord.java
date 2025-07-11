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

    //Foreign Key
    //Room - phòng
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    //Bed - giường
    @ManyToOne
    @JoinColumn(name = "bed_id")
    private Bed bed;


    //Patient - bênh nhân
    @ManyToOne
    @JoinColumn(name = "ho_so_kham_id")
    private MedicalRecord medicalRecord;
}
