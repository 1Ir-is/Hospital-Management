package com.example.hospital_management.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ImpatientBasicDto {
    private Long id;
    private String code;
    private String patientName;

    public ImpatientBasicDto(Long id, String code, String patientName) {
        this.id = id;
        this.code = code;
        this.patientName = patientName;
    }

}
