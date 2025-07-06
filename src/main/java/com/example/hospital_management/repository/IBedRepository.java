package com.example.hospital_management.repository;

import com.example.hospital_management.entity.AdvancePayment;
import com.example.hospital_management.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBedRepository extends JpaRepository<Bed, Long> {
}
