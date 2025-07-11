package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IRoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAllByDepartment_Id(Long id);
    @Query("SELECT r FROM Room r WHERE r.department.id = :departmentId AND r.roomType.id = 6")
    List<Room> findAllClinicRoomsByDepartment(@Param("departmentId") Long departmentId);

    @Query(value = "SELECT * FROM rooms where room_type_id = 6", nativeQuery = true)
    List<Room> findAllExaminationRoom();
}
