package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBedRepository extends JpaRepository<Bed, Long> {

    // Đếm số giường theo phòng
    @Query("SELECT COUNT(b) FROM Bed b WHERE b.room.id = :roomId")
    Long countByRoomId(@Param("roomId") Long roomId);

    // Lấy danh sách giường theo phòng
    @Query("SELECT b FROM Bed b WHERE b.room.id = :roomId ORDER BY b.number")
    List<Bed> findByRoomIdOrderByNumber(@Param("roomId") Long roomId);

    // Đếm số giường theo nhiều phòng cùng lúc
    @Query("SELECT b.room.id, COUNT(b) FROM Bed b WHERE b.room.id IN :roomIds GROUP BY b.room.id")
    List<Object[]> countBedsForRooms(@Param("roomIds") List<Long> roomIds);

    // Kiểm tra số giường có tồn tại trong phòng
    @Query("SELECT COUNT(b) > 0 FROM Bed b WHERE b.room.id = :roomId AND b.number = :bedNumber")
    boolean existsByRoomIdAndBedNumber(@Param("roomId") Long roomId, @Param("bedNumber") Integer bedNumber);

    // Tìm giường có số lớn nhất trong phòng
    @Query("SELECT MAX(b.number) FROM Bed b WHERE b.room.id = :roomId")
    Integer findMaxBedNumberInRoom(@Param("roomId") Long roomId);
}