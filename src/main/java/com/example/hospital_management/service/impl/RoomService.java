package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Room;
import com.example.hospital_management.repository.IRoomRepository;
import com.example.hospital_management.service.IRoomService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService implements IRoomService {
    private final IRoomRepository repository;

    public RoomService(IRoomRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Room> findAllExaminationRoom() {
        return repository.findAllExaminationRoom();
    }

    @Override
    public Optional<Room> findById(Long roomId) {
        return repository.findById(roomId);
    }
}
