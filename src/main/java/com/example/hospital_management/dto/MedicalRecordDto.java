package com.example.hospital_management.dto;

import com.example.hospital_management.entity.ExaminationShift;
import com.example.hospital_management.entity.Patient;
import com.example.hospital_management.entity.Room;
import com.example.hospital_management.entity.VitalSign;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordDto {
    private Long id;
    private String code;
    private Integer queueNumber;
    private LocalDate visitDate;
    private String symptom;
    private Boolean status;



    // Dữ liệu rút gọn từ các bảng liên kết
    private String patientName;
    private String roomName;
    private String bloodPressure;
    private Float weight;
    private Float height;
    private Integer heartRate;

    // patient
    private Long patientId;
    private LocalDate birthday;
    private Boolean gender;

    private Long examination_shift_id;

}
