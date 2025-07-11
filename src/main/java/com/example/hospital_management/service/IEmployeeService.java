package com.example.hospital_management.service;

import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.entity.TestOrder;

import java.util.List;
import java.util.Optional;

public interface IEmployeeService {
    List<Employee> findAll();
    Optional<Employee> findById(Long id);
}
