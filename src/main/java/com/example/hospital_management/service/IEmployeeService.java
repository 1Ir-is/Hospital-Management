package com.example.hospital_management.service;

import com.example.hospital_management.entity.Employee;

import java.util.Map;
import java.util.Optional;

import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.entity.TestOrder;

import java.util.List;
import java.util.Optional;

import com.example.hospital_management.entity.Employee;

public interface IEmployeeService {
    Optional<Employee> findById(Long id);
    List<Employee> findAll();
    Employee findEmployeeById(Long id);
}
