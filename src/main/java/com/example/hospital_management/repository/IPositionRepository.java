package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IPositionRepository extends JpaRepository<Position, Long> {
    @Query("SELECT p FROM Position p WHERE p.name = :name")
    Position findByName(@Param("name") String name);
}