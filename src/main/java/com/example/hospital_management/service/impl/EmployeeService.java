package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.entity.EmployeeRole;
import com.example.hospital_management.entity.Role;
import com.example.hospital_management.repository.IEmployeeRepository;
import com.example.hospital_management.repository.IEmployeeRoleRepository;
import com.example.hospital_management.repository.IRoleRepository;
import com.example.hospital_management.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements IEmployeeService {

    @Autowired
    private IEmployeeRepository employeeRepository;

    @Autowired
    private IEmployeeRoleRepository employeeRoleRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        if (employee.getStartingDate() == null) {
            employee.setStartingDate(LocalDate.now());
        }
        if (employee.getStatus() == null) {
            employee.setStatus(true);
        }
        return employeeRepository.save(employee);
    }

    @Override
    public Employee findEmployeeById(Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        return employeeOptional.orElse(null);
    }

    @Override
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void saveEmployeeWithRoles(Employee employee, List<Long> roleIds) {
        // luu employee
        Employee saveEmployee = saveEmployee(employee);

        // luu role cho nhan vien
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                Role role = roleRepository.findById(roleId).orElse(null);
                if (role != null) {
                    EmployeeRole employeeRole = new EmployeeRole();
                    employeeRole.setEmployee(saveEmployee);
                    employeeRole.setRole(role);
                    employeeRoleRepository.save(employeeRole);
                }
            }
        }
    }

    @Override
    @Transactional
    public void updateEmployeeRoles(Long employeeId, List<Long> roleIds) {
        // Xoa tat ca role cu
        employeeRoleRepository.deleteByEmployeeId(employeeId);

        // Them role moi
        Employee employee = findEmployeeById(employeeId);
        if (employee != null && roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                Role role = roleRepository.findById(roleId).orElse(null);
                if (role != null) {
                    EmployeeRole employeeRole = new EmployeeRole();
                    employeeRole.setEmployee(employee);
                    employeeRole.setRole(role);
                    employeeRoleRepository.save(employeeRole);
                }
            }
        }
    }
}
