package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.VitalSign;
import com.example.hospital_management.repository.IVitalSignRepository;
import com.example.hospital_management.service.IVitalSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VitalSignService implements IVitalSignService {
    private final IVitalSignRepository vitalSignRepository;

    @Autowired
    public VitalSignService(IVitalSignRepository vitalSignRepository) {
        this.vitalSignRepository = vitalSignRepository;
    }

    @Override
    public void save(VitalSign vitalSign) {
        vitalSignRepository.save(vitalSign);
    }

    @Override
    public List<VitalSign> findAll() {
        return vitalSignRepository.findAll();
    }

    @Override
    public VitalSign saved(VitalSign vitalSign) {
        return vitalSignRepository.save(vitalSign);
    }
}
