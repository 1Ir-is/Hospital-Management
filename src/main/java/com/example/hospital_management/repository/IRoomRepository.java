package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IRoomRepository extends JpaRepository<Room, Long> {
    @Query(value = "SELECT * FROM rooms where room_type_id = 6", nativeQuery = true)
    List<Room> findAllExaminationRoom();
}
