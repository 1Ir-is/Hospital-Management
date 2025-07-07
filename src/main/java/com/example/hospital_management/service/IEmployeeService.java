package com.example.hospital_management.service;

import com.example.hospital_management.entity.Employee;

import java.util.List;

public interface IEmployeeService {
    List<Employee> findByDepartment(Long departmentId);
}
