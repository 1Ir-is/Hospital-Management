package com.example.hospital_management.service;

import com.example.hospital_management.entity.Employee;

import java.util.List;

import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.entity.ImpatientRecord;
import com.example.hospital_management.entity.TestOrder;

import java.util.List;
import java.util.Optional;

import com.example.hospital_management.entity.Employee;

public interface IEmployeeService {
    List<Employee> findByDepartment(Long departmentId);

    List<Employee> findAllDoctors();

    List<Employee> findAllNurses();

    List<Employee> findDoctorsByDepartment(Long departmentId);

    List<Employee> findNursesByDepartment(Long departmentId);
    List<Employee> findAll();
    Optional<Employee> findById(Long id);
    Employee findEmployeeById(Long id);
}
