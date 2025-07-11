package com.example.hospital_management.service;


import com.example.hospital_management.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface IEmployeeAssignmentService {
    void assignDoctorToRecord(Long recordId, Long employeeId);

    Optional<Employee> findDoctorByRecordId(Long recordId);

    Optional<Employee> findNurseByRecordId(Long recordId);

    void assignNurseToRecord(Long recordId, Long nurseId);
}
