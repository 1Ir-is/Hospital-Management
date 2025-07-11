package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Bed;
import com.example.hospital_management.repository.IBedRepository;
import com.example.hospital_management.service.IBedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BedService implements IBedService {
    private IBedRepository bedRepository;
    @Autowired

    public BedService(IBedRepository bedRepository) {
        this.bedRepository = bedRepository;
    }

    @Override
    public List<Bed> findAll() {
        return bedRepository.findAll();
    }
}
