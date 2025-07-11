package com.example.hospital_management.service.impl;

import com.example.hospital_management.dto.RoomDTO;
import com.example.hospital_management.entity.Room;
import com.example.hospital_management.repository.IRoomRepository;
import com.example.hospital_management.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final IRoomRepository roomRepository;

    @Override
    public Page<RoomDTO> getAllRooms(Pageable pageable) {
        Page<Room> rooms = roomRepository.findAll(pageable);
        return rooms.map(this::convertToDTO);
    }

    @Override
    public Page<RoomDTO> searchRooms(String name, Integer number, Long departmentId,
                                     Long roomTypeId, Boolean status, Pageable pageable) {
        Page<Room> rooms = roomRepository.findWithFilters(name, number, departmentId,
                roomTypeId, status, pageable);
        return rooms.map(this::convertToDTO);
    }

    @Override
    public RoomDTO getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng với ID: " + id));
        return convertToDTO(room);
    }

    @Override
    public List<RoomDTO> getActiveRooms() {
        List<Room> rooms = roomRepository.findActiveRoomsOrderByDepartmentAndNumber();
        return rooms.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long getTotalRoomCount() {
        return roomRepository.count();
    }

    @Override
    public Long getActiveRoomCount() {
        return roomRepository.countActiveRooms();
    }

    @Override
    public Long getRoomCountByDepartment(Long departmentId) {
        return roomRepository.countByDepartmentId(departmentId);
    }

    private RoomDTO convertToDTO(Room room) {
        RoomDTO dto = new RoomDTO();
        dto.setId(room.getId());
        dto.setName(room.getName());
        dto.setDescription(room.getDescription());
        dto.setNumber(room.getNumber());
        dto.setStatus(room.getStatus());

        // Department info
        if (room.getDepartment() != null) {
            dto.setDepartmentId(room.getDepartment().getId());
            dto.setDepartmentName(room.getDepartment().getName());
        }

        // Room Type info
        if (room.getRoomType() != null) {
            dto.setRoomTypeId(room.getRoomType().getId());
            dto.setRoomTypeName(room.getRoomType().getName());
            dto.setRoomTypeDescription(room.getRoomType().getDescription());
        }

        // Additional info
        dto.setStatusText(room.getStatus() != null && room.getStatus() ? "Hoạt động" : "Không hoạt động");
        dto.setFullName((dto.getDepartmentName() != null ? dto.getDepartmentName() + " - " : "") +
                "Phòng " + room.getNumber());

        // Note: bedCount would need a separate query or be added to Room entity
        dto.setBedCount(0L); // Placeholder

        return dto;
    }
}