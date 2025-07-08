package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Department;
import com.example.hospital_management.repository.IDepartmentRepository;
import com.example.hospital_management.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService implements IDepartmentService {

    @Autowired
    private IDepartmentRepository departmentRepository;

    @Override
    public Page<Department> findAllDepartment(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }

    @Override
    public List<Department> findAllDepartmentsForDropdown() {
        return departmentRepository.findAll();
    }

    @Override
    public Department saveDepartment(Department department) {
        return departmentRepository.save(department);
    }

    @Override
    public Department findDepartmentById(Long id) {
        Optional<Department> departmentOptional = departmentRepository.findById(id);
        return departmentOptional.orElse(null);
    }

    @Override
    public void deleteDepartmentById(Long id) {
        departmentRepository.deleteById(id);
    }
}
