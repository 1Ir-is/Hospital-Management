package com.example.hospital_management.service;

import com.example.hospital_management.entity.Test;

import java.util.List;
import java.util.Optional;

public interface ITestService {
    List<Test> findAll();

    Optional<Test> findById(Long id);
}
