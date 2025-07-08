package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.EmployeeRole;
import com.example.hospital_management.repository.IEmployeeRoleRepository;
import com.example.hospital_management.service.IEmployeeRoleRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeRoleService implements IEmployeeRoleRService {

    @Autowired
    private IEmployeeRoleRepository employeeRoleRepository;

    @Override
    public List<EmployeeRole> findByEmployeeId(Long id) {
        return employeeRoleRepository.findByEmployeeId(id);
    }
}

