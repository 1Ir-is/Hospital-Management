package com.example.hospital_management.dto;


import java.util.Date;

public interface PrescriptionRequestDto {
    Integer getId();
    String getPatientsName();
    String getMedicalRecordCode();
    Date getCreatedDate();
    String getStatus();
}