package com.example.hospital_management.service;

import com.example.hospital_management.entity.Employee;

public interface IEmployeeService {
    Employee findEmployeeById(Long id);
}
