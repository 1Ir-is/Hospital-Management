package com.example.hospital_management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestRequestDto {
    private Long id;
    private String patientName;
    private Long patientId;
    private String name;
    private String note;
    private String status;

}
