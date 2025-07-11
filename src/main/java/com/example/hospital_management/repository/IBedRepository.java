package com.example.hospital_management.repository;

import com.example.hospital_management.entity.Bed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IBedRepository extends JpaRepository<Bed, Long> {
    @Query(
            value = "SELECT DISTINCT b.* " +
                    "FROM beds b " +
                    "LEFT JOIN impatient_records ir ON b.id = ir.bed_id " +
                    "WHERE ir.id IS NULL OR ir.discharge_date IS NOT NULL",

            countQuery = "SELECT COUNT(DISTINCT b.id) " +
                    "FROM beds b " +
                    "LEFT JOIN impatient_records ir ON b.id = ir.bed_id " +
                    "WHERE ir.id IS NULL OR ir.discharge_date IS NOT NULL",

            nativeQuery = true
    )
    List<Bed> getListBedNotUsage();


    @Query("SELECT b FROM Bed b " +
            "JOIN b.room r " +
            "LEFT JOIN ImpatientRecord ir ON b.id = ir.bed.id " +
            "WHERE (ir.id IS NULL OR ir.dischargeDate IS NOT NULL) " +
            "AND r.id = :roomId")
    List<Bed> findAvailableBedsByRoomId(@Param("roomId") Integer roomId);
}
