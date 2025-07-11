package com.example.hospital_management.service;

import com.example.hospital_management.entity.Department;

import java.util.List;

public interface IDepartmentService {
    List<Department> findAll();
}
