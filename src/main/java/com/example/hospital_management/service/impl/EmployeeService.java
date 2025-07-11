package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Employee;
import com.example.hospital_management.repository.IEmployeeRepository;
import com.example.hospital_management.service.IEmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService implements IEmployeeService {
    private final IEmployeeRepository iEmployeeRepository;

    public EmployeeService(IEmployeeRepository iEmployeeRepository) {
        this.iEmployeeRepository = iEmployeeRepository;
    }

    @Override
    public Employee findEmployeeById(Long id) {
        return iEmployeeRepository.findEmployeeById(id);
    }
}
