package com.example.hospital_management.service;

import com.example.hospital_management.entity.Employee;

import java.util.Map;
import java.util.Optional;

public interface IEmployeeService {
    Optional<Employee> findById(Long id);
}
