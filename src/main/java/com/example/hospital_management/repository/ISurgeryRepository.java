package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Surgery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISurgeryRepository extends JpaRepository<Surgery, Long> {
}
