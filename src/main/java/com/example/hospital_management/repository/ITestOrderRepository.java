package com.example.hospital_management.repository;

import com.example.hospital_management.entity.TestOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITestOrderRepository extends JpaRepository<TestOrder, Long> {
}
