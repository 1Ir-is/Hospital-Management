package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.repository.IEmployeeRepository;
import com.example.hospital_management.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService implements IEmployeeService {
    @Autowired
    private IEmployeeRepository employeeRepository;

    @Override
    public List<Employee> findByDepartment(Long departmentId) {
        return employeeRepository.findByDepartment_Id(departmentId);
    }

    @Override
    public List<Employee> findAllDoctors() {
        return employeeRepository.findAllDoctors();
    }

    @Override
    public List<Employee> findAllNurses() {
        return employeeRepository.findAllNurses();
    }

    @Override
    public List<Employee> findDoctorsByDepartment(Long departmentId) {
        return employeeRepository.findDoctorsByDepartment(departmentId);
    }

    @Override
    public List<Employee> findNursesByDepartment(Long departmentId) {
        return employeeRepository.findNursesByDepartment(departmentId);
    }
}
