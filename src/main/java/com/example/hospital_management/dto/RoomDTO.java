package com.example.hospital_management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private Long id;
    private String name;
    private String description;
    private Integer number;
    private Boolean status;

    // Department info
    private Long departmentId;
    private String departmentName;

    // Room Type info
    private Long roomTypeId;
    private String roomTypeName;
    private String roomTypeDescription;

    // Additional info for display
    private Long bedCount;
    private String statusText;
    private String fullName; // Combines department + room name
}