package com.example.hospital_management.service;

import com.example.hospital_management.entity.Insurance;

public interface IInsuranceService {
    void save(Insurance insurance);
    boolean existsByCode(String code);
    Insurance findByCode(String code);
}
