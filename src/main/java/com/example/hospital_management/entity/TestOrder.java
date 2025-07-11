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
@Table(name = "test_orders")
public class TestOrder { // Chỉ định xét nghiệm
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private String note;
    private String result;
    private Boolean status;
    private boolean payStatus;
    @Column(name = "image_path")
    private String imagePath;

    //Foreign Key
    //Impatient Record - hồ sơ nhập viện (nội trú)
    @ManyToOne
    @JoinColumn(name = "impatient_record_id")
    private ImpatientRecord impatientRecord;

    //Employee - Nhân viên (Bác sĩ chỉ định)
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    //Test - Xét nghiệm
    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    @ManyToOne
    @JoinColumn(name = "test_status_id")
    private TestStatus testStatus;
}
