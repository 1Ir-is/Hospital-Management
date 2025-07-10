package com.example.hospital_management.service;


import com.example.hospital_management.entity.EmployeeRole;

import java.util.List;

public interface IEmployeeRoleRService {
    List<EmployeeRole> findByEmployeeId(Long id);
}
