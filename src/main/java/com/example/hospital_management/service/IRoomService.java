package com.example.hospital_management.service;

import com.example.hospital_management.dto.RoomDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IRoomService {
    Page<RoomDTO> getAllRooms(Pageable pageable);

    Page<RoomDTO> searchRooms(String name, Integer number, Long departmentId,
                              Long roomTypeId, Boolean status, Pageable pageable);

    RoomDTO getRoomById(Long id);

    List<RoomDTO> getActiveRooms();

    Long getTotalRoomCount();

    Long getActiveRoomCount();

    Long getRoomCountByDepartment(Long departmentId);

    // Thêm methods mới cho bed count
    Map<Long, Long> getBedCountsForRooms(List<Long> roomIds);

    Long getBedCountForRoom(Long roomId);
}