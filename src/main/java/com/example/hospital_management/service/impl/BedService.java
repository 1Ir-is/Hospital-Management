package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Bed;
import com.example.hospital_management.repository.IBedRepository;
import com.example.hospital_management.service.IBedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BedService implements IBedService {
    private final IBedRepository iBedRepository;

    public BedService(IBedRepository iBedRepository) {
        this.iBedRepository = iBedRepository;
    }

    @Override
    public List<Bed> getListBedNotUsage() {
        return iBedRepository.getListBedNotUsage();
    }

    @Override
    public List<Bed> findAvailableBedsByRoomId(Integer roomId) {
        return iBedRepository.findAvailableBedsByRoomId(roomId);
    }


    @Override
    public List<Bed> findAll() {
        return iBedRepository.findAll();
    }
}
