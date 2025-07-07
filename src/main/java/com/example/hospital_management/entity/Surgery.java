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
@Table(name = "surgeries")
public class Surgery { // Phẫu thuật
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private String result;
    private String note;

    //Foreign Key
    //Impatient - hồ sơ nhập viện
    @ManyToOne
    @JoinColumn(name = "impatient_record_id")
    private ImpatientRecord impatientRecord;

    //Employee - Nhân viên
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    //Surgery Type - Loại phẫu thuật
    @ManyToOne
    @JoinColumn(name = "surgery_type_id")
    private SurgeryType surgeryType;

}
