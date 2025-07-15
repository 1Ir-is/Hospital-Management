package com.example.hospital_management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public interface TestRequestDto {
    Long getId();
    String getPatientName();
    Long getPatientId();
    String getMedicalRecordCode();  // Thêm trường mới
    String getName();
    String getNote();
    String getStatus();
}
