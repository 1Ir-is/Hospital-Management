package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Test;
import com.example.hospital_management.repository.ITestRepository;
import com.example.hospital_management.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestService implements ITestService {
    ITestRepository testRepository;
    @Autowired
    public TestService(ITestRepository testRepository) {
        this.testRepository = testRepository;
    }


    @Override
    public List<Test> findAll() {
        return testRepository.findAll();
    }

    @Override
    public Optional<Test> findById(Long id) {
        return testRepository.findById(id);
    }
}
