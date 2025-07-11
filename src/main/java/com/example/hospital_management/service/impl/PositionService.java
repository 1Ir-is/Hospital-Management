package com.example.hospital_management.service.impl;

import com.example.hospital_management.entity.Position;
import com.example.hospital_management.repository.IPositionRepository;
import com.example.hospital_management.service.IPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionService implements IPositionService {

    @Autowired
    private IPositionRepository positionRepository;

    @Override
    public List<Position> findAllPositions() {
        return positionRepository.findAll();
    }

    @Override
    public Position savePosition(Position position) {
        return positionRepository.save(position);
    }

    @Override
    public Position findPositionById(Long id) {
        Optional<Position> positionOptional = positionRepository.findById(id);
        return positionOptional.orElse(null);
    }

    @Override
    public void deletePositionById(Long id) {
        positionRepository.deleteById(id);
    }
}
