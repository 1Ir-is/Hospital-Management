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

    public RoomService(IRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }
}
