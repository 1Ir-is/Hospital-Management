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
@Table(name = "medicines")
public class Medicine { // Thuốc
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long price;

    //Foreign Key
    //Medicine Type - Loại thuốc
    @ManyToOne
    @JoinColumn(name = "medicine_type_id")
    private MedicineType medicineType;

    @ManyToOne
    @JoinColumn(name = "medicine_unit_id")
    private MedicineUnit medicineUnit;
}
