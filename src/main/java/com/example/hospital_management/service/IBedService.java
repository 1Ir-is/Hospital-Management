package com.example.hospital_management.service;

import com.example.hospital_management.entity.Bed;

import java.util.List;

public interface IBedService {
    List<Bed> getListBedNotUsage();
    List<Bed> findAvailableBedsByRoomId(Integer roomId);
}
