package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Test;
import com.example.hospital_management.repository.ITestRepository;
import com.example.hospital_management.service.ITestService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestService implements ITestService {
    private final ITestRepository repository;

    public TestService(ITestRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Test> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Test> findById(Long id) {
        return repository.findById(id);
    }
}
