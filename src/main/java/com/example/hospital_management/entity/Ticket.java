package com.example.hospital_management.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int queueNumber;
    private LocalDateTime createdAt;
    private String idCard;
    private String name;
    private String phone;
    private String email;
    private LocalDate appointmentDate;
    private boolean isPriority;
    private boolean isCalled = false;
}

