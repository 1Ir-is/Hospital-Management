package com.example.hospital_management.service;

import com.example.hospital_management.entity.VitalSign;

import java.util.List;

public interface IVitalSignService {
    void save(VitalSign vitalSign);
    List<VitalSign> findAll();
    VitalSign saved(VitalSign vitalSign);

}
