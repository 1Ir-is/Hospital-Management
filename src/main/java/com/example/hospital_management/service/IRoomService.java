package com.example.hospital_management.service;

import com.example.hospital_management.entity.Room;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.hospital_management.entity.Room;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.example.hospital_management.entity.Room;

import java.util.List;

public interface IRoomService {
    List<Room> findAllExaminationRoom();

    /////////
//    Optional<Room> findById(Long roomId);

    List<Room> findAll();
    List<Room> findAllByDepartment_Id(Long id);
    Room findById(Long id);
    List<Room> findAllClinicRoomsByDepartment(@Param("departmentId") Long departmentId);

    List<Room> findAllTestRoom();

}
