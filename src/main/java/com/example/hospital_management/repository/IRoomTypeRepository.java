package com.example.hospital_management.repository;

import com.example.hospital_management.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRoomTypeRepository extends JpaRepository<RoomType, Long> {

    @Query("SELECT rt FROM RoomType rt WHERE rt.name = :name")
    RoomType findByName(@Param("name") String name);

    @Query("SELECT rt FROM RoomType rt ORDER BY rt.name")
    List<RoomType> findAllOrderByName();
}