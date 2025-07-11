package com.example.hospital_management.service;

import com.example.hospital_management.entity.Position;

import java.util.List;

public interface IPositionService {
    List<Position> findAllPositions();

    Position savePosition(Position position);

    Position findPositionById(Long id);

    void deletePositionById(Long id);
}
