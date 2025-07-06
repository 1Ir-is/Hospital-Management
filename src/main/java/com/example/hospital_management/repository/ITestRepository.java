package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITestRepository extends JpaRepository<Test, Long> {
}
