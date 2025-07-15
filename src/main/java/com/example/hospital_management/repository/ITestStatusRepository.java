package com.example.hospital_management.repository;

import com.example.hospital_management.entity.TestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITestStatusRepository extends JpaRepository<TestStatus,Long> {
}
