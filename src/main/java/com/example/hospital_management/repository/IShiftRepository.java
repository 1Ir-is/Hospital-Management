package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IShiftRepository extends JpaRepository<Shift, Long> {
}
