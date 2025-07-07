package com.example.hospital_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rooms")
public class Room { // Phòng
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer number;
    private Boolean status;

    //Foreign Key
    //Room type - loại phongf
    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    //Department - Khoa
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
