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
@Table(name = "advance_payments")
public class AdvancePayment { // Tạm ứng viện phí
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long fee;
    private LocalDate date;
    private String reason;

    //Foreign Key
    @ManyToOne
    @JoinColumn(name = "impatient_record_id")
    private ImpatientRecord impatientRecord;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
