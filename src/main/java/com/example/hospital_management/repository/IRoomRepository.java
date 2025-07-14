package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRoomRepository extends JpaRepository<Room, Long> {

    // Tìm kiếm phòng theo tên
    @Query("SELECT r FROM Room r WHERE r.name LIKE %:name%")
    Page<Room> findByNameContaining(@Param("name") String name, Pageable pageable);

    // Tìm kiếm phòng theo số phòng
    @Query("SELECT r FROM Room r WHERE r.number = :number")
    Room findByNumber(@Param("number") Integer number);

    // Tìm kiếm phòng theo khoa
    @Query("SELECT r FROM Room r WHERE r.department.id = :departmentId")
    Page<Room> findByDepartmentId(@Param("departmentId") Long departmentId, Pageable pageable);

    // Tìm kiếm phòng theo loại phòng
    @Query("SELECT r FROM Room r WHERE r.roomType.id = :roomTypeId")
    Page<Room> findByRoomTypeId(@Param("roomTypeId") Long roomTypeId, Pageable pageable);

    // Tìm kiếm phòng theo trạng thái
    @Query("SELECT r FROM Room r WHERE r.status = :status")
    Page<Room> findByStatus(@Param("status") Boolean status, Pageable pageable);

    List<Room> findAllByDepartment_Id(Long id);

    @Query("SELECT r FROM Room r WHERE r.department.id = :departmentId AND r.roomType.id = 6")
    List<Room> findAllClinicRoomsByDepartment(@Param("departmentId") Long departmentId);

    @Query(value = "SELECT * FROM rooms where room_type_id = 6", nativeQuery = true)
    List<Room> findAllExaminationRoom();

    // Tìm kiếm phòng với phòng xét nghiệm
    @Query(value = "SELECT * FROM rooms where room_type_id = 7", nativeQuery = true)
    List<Room> findAllTestRoom();

    // Tìm kiếm phòng với filter tổng hợp
    @Query("SELECT r FROM Room r WHERE " + "(:name IS NULL OR r.name LIKE %:name%) AND " + "(:number IS NULL OR r.number = :number) AND " + "(:departmentId IS NULL OR r.department.id = :departmentId) AND " + "(:roomTypeId IS NULL OR r.roomType.id = :roomTypeId) AND " + "(:status IS NULL OR r.status = :status)")
    Page<Room> findWithFilters(@Param("name") String name, @Param("number") Integer number, @Param("departmentId") Long departmentId, @Param("roomTypeId") Long roomTypeId, @Param("status") Boolean status, Pageable pageable);

    // Đếm số phòng theo khoa
    @Query("SELECT COUNT(r) FROM Room r WHERE r.department.id = :departmentId")
    Long countByDepartmentId(@Param("departmentId") Long departmentId);

    // Đếm số phòng đang hoạt động
    @Query("SELECT COUNT(r) FROM Room r WHERE r.status = true")
    Long countActiveRooms();

    // Lấy danh sách phòng đang hoạt động
    @Query("SELECT r FROM Room r WHERE r.status = true ORDER BY r.department.name, r.number")
    List<Room> findActiveRoomsOrderByDepartmentAndNumber();



}
