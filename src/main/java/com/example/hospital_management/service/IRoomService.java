package com.example.hospital_management.service;

import com.example.hospital_management.dto.RoomDTO;
import com.example.hospital_management.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IRoomService {
    Page<RoomDTO> getAllRooms(Pageable pageable);

    Page<RoomDTO> searchRooms(String name, Integer number, Long departmentId,
                              Long roomTypeId, Boolean status, Pageable pageable);

    RoomDTO getRoomById(Long id);

    List<RoomDTO> getActiveRooms();

    Long getTotalRoomCount();

    Long getActiveRoomCount();

    Long getRoomCountByDepartment(Long departmentId);

    List<Room> findAllExaminationRoom();

    /// //////
//    Optional<Room> findById(Long roomId);

    List<Room> findAll();

    List<Room> findAllByDepartment_Id(Long id);

    Room findById(Long id);

    List<Room> findAllClinicRoomsByDepartment(@Param("departmentId") Long departmentId);

    List<Room> findAllTestRoom();
}