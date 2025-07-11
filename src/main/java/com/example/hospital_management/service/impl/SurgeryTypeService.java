package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.SurgeryType;
import com.example.hospital_management.repository.ISurgeryTypeRepository;
import com.example.hospital_management.service.ISurgeryTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SurgeryTypeService implements ISurgeryTypeService {
    private ISurgeryTypeRepository surgeryTypeRepository;
    @Autowired
    public SurgeryTypeService(ISurgeryTypeRepository surgeryTypeRepository) {
        this.surgeryTypeRepository = surgeryTypeRepository;
    }

    @Override
    public List<SurgeryType> findAll() {
        return surgeryTypeRepository.findAll();
    }
}
