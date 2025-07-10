package com.example.hospital_management.service;

import com.example.hospital_management.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IEmployeeService {
    List<Employee> findAllEmployees();

    Page<Employee> findEmployeesWithFilters(String search, String status, Pageable pageable);

    long countTotalEmployees();

    long countActiveEmployees();

    long countInactiveEmployees();

    long countNewEmployeesThisMonth();

    Employee saveEmployee(Employee employee);

    Employee findEmployeeById(Long id);

    void deleteEmployeeById(Long id);

    void saveEmployeeWithRoles(Employee employee, List<Long> roleIds);

    void updateEmployeeRoles(Long employeeId, List<Long> roleIds);

    void updateEmployeeWithRoles(Long employeeId, Employee updatedEmployee, List<Long> roleIds);
}