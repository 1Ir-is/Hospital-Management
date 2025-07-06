package com.example.hospital_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "work_schedules")
public class WorkSchedule { // Lịch phân công
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;

    //Foreign Key
    //Room - Phòng
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    //Employee - Nhân viên
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    //Shift - Ca làm
    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;
}
