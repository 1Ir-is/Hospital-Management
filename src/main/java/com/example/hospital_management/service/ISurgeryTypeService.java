package com.example.hospital_management.service;

import com.example.hospital_management.entity.SurgeryType;

import java.util.List;

public interface ISurgeryTypeService {
    List<SurgeryType> findAll();
}
