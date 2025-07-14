package com.example.hospital_management.service;

import com.example.hospital_management.entity.Employee;

import java.util.List;

import com.example.hospital_management.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IEmployeeService {
    List<Employee> findByDepartment(Long departmentId);

    List<Employee> findAllDoctors();

    List<Employee> findAllNurses();

    List<Employee> findDoctorsByDepartment(Long departmentId);

    List<Employee> findNursesByDepartment(Long departmentId);
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

    Optional<Employee> findById(Long id);

    List<Employee> findAll();

    void updateEmployeeRoles(Long employeeId, List<Long> roleIds);

    void updateEmployeeWithRoles(Long employeeId, Employee updatedEmployee, List<Long> roleIds);
    Optional<Employee> findEmployeeByEmail(String email);

    Employee findByEmail(String email);
}