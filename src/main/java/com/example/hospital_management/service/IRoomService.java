package com.example.hospital_management.service;

import com.example.hospital_management.entity.Room;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IRoomService {
    List<Room> findAllExaminationRoom();

    Optional<Room> findById(Long roomId);
}
