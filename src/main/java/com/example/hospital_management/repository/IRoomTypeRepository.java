package com.example.hospital_management.repository;

import com.example.hospital_management.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoomTypeRepository extends JpaRepository<RoomType, Long> {
}
