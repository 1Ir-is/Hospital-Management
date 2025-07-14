package com.example.hospital_management.service;

import com.example.hospital_management.entity.SurgeryType;

import java.util.List;
import java.util.Optional;

public interface ISurgeryTypeService {
    List<SurgeryType> findAll();
    Optional<SurgeryType> findById(Long id);
}
