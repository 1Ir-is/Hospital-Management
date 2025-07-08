package com.example.hospital_management.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EmployeeFormDTO {
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
    private Long departmentId;
    private Long positionId;
    private List<Long> roleIds;
}
