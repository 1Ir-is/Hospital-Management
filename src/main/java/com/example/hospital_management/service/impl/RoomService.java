package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Room;
import com.example.hospital_management.repository.IRoomRepository;
import com.example.hospital_management.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService implements IRoomService {
    private final IRoomRepository roomRepository;

    @Autowired
    public RoomService(IRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> findAllByDepartment_Id(Long id) {
        return roomRepository.findAllByDepartment_Id(id);
    }

    @Override
    public Room findById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    @Override
    public List<Room> findAllClinicRoomsByDepartment(Long departmentId) {
        return roomRepository.findAllClinicRoomsByDepartment(departmentId);
    }
}
