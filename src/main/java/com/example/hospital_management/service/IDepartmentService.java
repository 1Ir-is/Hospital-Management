package com.example.hospital_management.service;

import com.example.hospital_management.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDepartmentService {
    Page<Department> findAllDepartment(Pageable pageable);

    List<Department> findAllDepartmentsForDropdown();

    Department saveDepartment(Department department);

    Department findDepartmentById(Long id);

    void deleteDepartmentById(Long id);

    List<Department> findAll();
}
