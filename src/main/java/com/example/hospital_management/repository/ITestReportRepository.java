package com.example.hospital_management.repository;

import com.example.hospital_management.entity.TestReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITestReportRepository extends JpaRepository<TestReport, Long> {
}
