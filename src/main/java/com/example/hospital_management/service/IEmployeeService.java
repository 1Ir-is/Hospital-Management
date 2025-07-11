package com.example.hospital_management.service;

import com.example.hospital_management.entity.Employee;

import java.util.List;

public interface IEmployeeService {
    List<Employee> findByDepartment(Long departmentId);

    List<Employee> findAllDoctors();

    List<Employee> findAllNurses();

    List<Employee> findDoctorsByDepartment(Long departmentId);

    List<Employee> findNursesByDepartment(Long departmentId);
}
