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
@Table(name = "medical_records")

public class MedicalRecord { // Hồ sơ khám
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Integer queueNumber;
    private LocalDate visitDate;
    private String note;
    private String conclusion;
    private Boolean status;
    private Long fee;
    private LocalDate followupDate;
    private String symptom; // triệu chứng

    @Column(name = "payment_status")
    private Boolean paymentStatus = false;

    //Foreign Key
    //Patient - Bệnh nhân
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    //Room - phòng
//    @ManyToOne
//    @JoinColumn(name = "room_id")
//    private Room room;

    @ManyToOne
    @JoinColumn(name = "examtination_shift_id")
    private ExaminationShift examinationShift;
    @ManyToOne
    @JoinColumn(name = "vital_sign_id")
    private VitalSign vitalSign;

}
