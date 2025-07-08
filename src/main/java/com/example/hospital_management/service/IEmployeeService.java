package com.example.hospital_management.service;

import com.example.hospital_management.entity.Employee;

import java.util.List;

public interface IEmployeeService {
    List<Employee> findAllEmployees();

    Employee saveEmployee(Employee employee);

    Employee findEmployeeById(Long id);

    void deleteEmployeeById(Long id);

    void saveEmployeeWithRoles(Employee employee, List<Long> roleIds);

    void updateEmployeeRoles(Long employeeId, List<Long> roleIds);
}
