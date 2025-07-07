package com.example.hospital_management.repository;

import com.example.hospital_management.entity.MedicalRecord;
import com.example.hospital_management.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface IMedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    @Query("SELECT r FROM Room r JOIN MedicalRecord mr ON r.id = mr.room.id WHERE mr.code = :code")
    Room findRoomByCode(@Param("code") String code);


}
