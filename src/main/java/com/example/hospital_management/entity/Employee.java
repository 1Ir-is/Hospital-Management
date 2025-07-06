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
@Table(name = "employees")
public class Employee { // Nhân viên
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private Boolean gender;
    private String address;
    private LocalDate startingDate;
    private Boolean status;
    private String avatar;

    //Foreign Key
    //1. Department: Khoa
    @ManyToOne
    @JoinColumn(name = "deparment_id")
    private Department department;

    //2. Position: Chức vụ
    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

}
