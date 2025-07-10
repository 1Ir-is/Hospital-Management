package com.example.hospital_management.service.impl;

import com.example.hospital_management.dto.RoomDTO;
import com.example.hospital_management.entity.Room;
import com.example.hospital_management.repository.IBedRepository;
import com.example.hospital_management.repository.IRoomRepository;
import com.example.hospital_management.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {

    private final IRoomRepository roomRepository;
    private final IBedRepository bedRepository;

    @Override
    public Page<RoomDTO> getAllRooms(Pageable pageable) {
        Page<Room> rooms = roomRepository.findAll(pageable);
        return convertPageToDTO(rooms);
    }

    @Override
    public Page<RoomDTO> searchRooms(String name, Integer number, Long departmentId,
                                     Long roomTypeId, Boolean status, Pageable pageable) {
        Page<Room> rooms = roomRepository.findWithFilters(name, number, departmentId,
                roomTypeId, status, pageable);
        return convertPageToDTO(rooms);
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

    @Override
    public Map<Long, Long> getBedCountsForRooms(List<Long> roomIds) {
        List<Object[]> results = bedRepository.countBedsForRooms(roomIds);
        return results.stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],  // roomId
                        row -> (Long) row[1]   // bedCount
                ));
    }

    @Override
    public Long getBedCountForRoom(Long roomId) {
        return bedRepository.countByRoomId(roomId);
    }

    private Page<RoomDTO> convertPageToDTO(Page<Room> rooms) {
        // Lấy danh sách roomIds để query bed counts một lần
        List<Long> roomIds = rooms.getContent().stream()
                .map(Room::getId)
                .collect(Collectors.toList());

        // Lấy bed counts cho tất cả rooms
        Map<Long, Long> bedCounts = getBedCountsForRooms(roomIds);

        // Convert rooms to DTOs với bed counts
        return rooms.map(room -> convertToDTO(room, bedCounts.getOrDefault(room.getId(), 0L)));
    }

    private RoomDTO convertToDTO(Room room) {
        Long bedCount = getBedCountForRoom(room.getId());
        return convertToDTO(room, bedCount);
    }

    private RoomDTO convertToDTO(Room room, Long bedCount) {
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

        // Bed information
        dto.setBedCount(bedCount);
        dto.setBedInfo(formatBedInfo(bedCount));
        dto.setCapacityInfo(formatCapacityInfo(bedCount));

        // Additional info
        dto.setStatusText(room.getStatus() != null && room.getStatus() ? "Hoạt động" : "Không hoạt động");
        dto.setFullName((dto.getDepartmentName() != null ? dto.getDepartmentName() + " - " : "") +
                "Phòng " + room.getNumber() + " (" + dto.getBedInfo() + ")");

        return dto;
    }

    private String formatBedInfo(Long bedCount) {
        if (bedCount == null || bedCount == 0) {
            return "Không có giường";
        } else if (bedCount == 1) {
            return "1 giường";
        } else {
            return bedCount + " giường";
        }
    }

    private String formatCapacityInfo(Long bedCount) {
        if (bedCount == null || bedCount == 0) {
            return "Phòng không giường";
        } else {
            return "Phòng " + bedCount + " giường";
        }
    }
}